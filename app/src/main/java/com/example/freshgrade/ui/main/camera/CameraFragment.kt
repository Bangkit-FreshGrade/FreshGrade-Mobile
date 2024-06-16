package com.example.freshgrade.ui.main.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.bumptech.glide.Glide
import com.example.freshgrade.R
import com.example.freshgrade.adapter.ImgCamAdapter
import com.example.freshgrade.databinding.FragmentCameraBinding
import com.example.freshgrade.ui.decoration.CarouselItemDecoration
import java.io.ByteArrayOutputStream

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private val cameraViewModel: CameraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

        val adapter = ImgCamAdapter(emptyList())
        binding.carouselCamRv.adapter = adapter
        binding.carouselCamRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.carouselCamRv)
        binding.carouselCamRv.itemAnimator = DefaultItemAnimator()
        binding.carouselCamRv.scrollToPosition(2)

        binding.carouselCamRv.addItemDecoration(CarouselItemDecoration())

        cameraViewModel.items.observe(viewLifecycleOwner, Observer { items ->
            adapter.updateItems(items)
        })

        val imageView = binding.infoIv
        val textView = binding.descTv

        imageView.setOnClickListener {
            imageView.visibility = View.GONE
            textView.visibility = View.VISIBLE
        }

        textView.setOnClickListener {
            textView.visibility = View.GONE
            imageView.visibility = View.VISIBLE
        }

        binding.cameraBtn.setOnClickListener { openCamera() }
        binding.galleryBtn.setOnClickListener { openGallery() }

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
        val chooser = Intent.createChooser(intent, "Pilih Gambar")
        launcherGallery.launch(chooser)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            selectedImageUri = result.data?.data as Uri
//            uriToFile(selectedImageUri as Uri)
//            val myFile = uriToFile(selectedImg)
//            selectedImageUri = myFile
//            startUCrop(currentImageUri!!)
//            binding.previewImageView.setImageURI(selectedImageUri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                REQUEST_CODE_CAMERA -> {
//                    val bitmap = data?.extras?.get("data") as Bitmap
//                    val uri = getImageUriFromBitmap(bitmap)
//                    uri?.let {
//                        selectedImageUri = it
//                        Glide.with(this).load(it).into(binding.previewImageView)
//                    }
//                }
//                REQUEST_CODE_GALLERY -> {
//                    data?.data?.let {
//                        selectedImageUri = it
//                        Glide.with(this).load(it).into(binding.previewImageView)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri? {
//        val bytes = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//        val path = MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap, "Title", null)
//        return Uri.parse(path)
//    }

    companion object {
        private const val REQUEST_CODE_CAMERA = 1
        private const val REQUEST_CODE_GALLERY = 2
        private const val REQUEST_CODE_PERMISSIONS = 3
    }

}