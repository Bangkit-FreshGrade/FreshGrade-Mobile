package com.example.freshgrade.ui.main.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.bumptech.glide.Glide
import com.example.freshgrade.R
import com.example.freshgrade.adapter.ImgCamAdapter
import com.example.freshgrade.data.api.ApiConfig
import com.example.freshgrade.data.api.ApiService
import com.example.freshgrade.data.repo.UserRepository
import com.example.freshgrade.data.response.ScanResponse
import com.example.freshgrade.databinding.FragmentCameraBinding
import com.example.freshgrade.ui.decoration.CarouselItemDecoration
import com.example.freshgrade.ui.main.result.ResultFragment
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

@Suppress("DEPRECATION")
class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService
    private val cameraViewModel: CameraViewModel by viewModels()
    private lateinit var userRepo : UserRepository

    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = ApiConfig.getApiService(requireContext())

        val imageView = binding.palceholderIv


        binding.cameraBtn.setOnClickListener { openCamera() }
        binding.galleryBtn.setOnClickListener { openGallery() }
        binding.scanBtn.setOnClickListener{
            binding.progressBarScan.visibility = View.VISIBLE
            val multipartBody = convertImageViewToMultipart(imageView, "image")
            predict(apiService,multipartBody)}
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pick a photo")
        launcherGallery.launch(chooser)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            selectedImageUri = result.data?.data as Uri
            binding.palceholderIv.setImageURI(selectedImageUri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_CAMERA -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    val uri = getImageUriFromBitmap(bitmap)
                    uri?.let {
                        selectedImageUri = it
                        Glide.with(this).load(it).into(binding.palceholderIv)
                    }
                }
                REQUEST_CODE_GALLERY -> {
                    data?.data?.let {
                        selectedImageUri = it
                        Glide.with(this).load(it).into(binding.palceholderIv)
                    }
                }
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    private fun convertImageViewToMultipart(imageView: ImageView, fieldName: String): MultipartBody.Part {
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageInByte = byteArrayOutputStream.toByteArray()

        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageInByte)
        return MultipartBody.Part.createFormData(fieldName, "image.jpg", requestBody)
    }

    private fun predict(apiService: ApiService, image: MultipartBody.Part) {

        Log.d(TAG, "uploadImage: Uploading image")
        val call = apiService.uploadImage(image)
        call.enqueue(object : Callback<ScanResponse> {
            override fun onResponse(call: Call<ScanResponse>, response: Response<ScanResponse>) {
                if (response.isSuccessful) {
                    binding.progressBarScan.visibility = View.GONE
                    val scanResponse = response.body()
                    Log.d(TAG, "onResponse: Upload successful ${response.body()}")
                    showToast("Upload successful!")

                    val bundle = Bundle().apply {
                        putString("id", scanResponse?.id)
                        putString("createdAt", scanResponse?.createdAt)
                        putString("fruit", scanResponse?.fruit)
                        putDouble("value", scanResponse?.value ?: 0.0)
                        putString("disease", scanResponse?.disease)
                        putString("imageUrl", scanResponse?.imageUrl)
                        putString("createdById", scanResponse?.createdById)
                    }
                    val navController = findNavController()
                    navController.navigate(R.id.action_navigation_camera_to_navigation_result, bundle)

                } else {
                    val errorResponse = response.errorBody()?.string()
                    Log.d(TAG, "onResponse: Upload failed: $errorResponse")
                    showToast("Upload failed: $errorResponse")
                }
            }

            override fun onFailure(call: Call<ScanResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: Upload error: ${t.message}")
                showToast("Upload error: ${t.message}")
            }
        })
    }

//    private fun moveToResult() {
//        selectedImageUri?.let {
//            val navController = findNavController()
//            val action = CameraFragmentDirections.actionNavigationCameraToResultFragment(it.toString())
//            navController.navigate(action)
//        }
//    }

    private fun showToast(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_CODE_CAMERA = 1
        private const val REQUEST_CODE_GALLERY = 2
        private const val REQUEST_CODE_PERMISSIONS = 3
        private val TAG = "CamFeature"
    }

}