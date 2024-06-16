package com.example.freshgrade.ui.main.result

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.freshgrade.R
import com.example.freshgrade.databinding.FragmentResultBinding

@Suppress("DEPRECATION")
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val resultViewModel: ResultViewModel by viewModels()

    private var selectedImageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(EXTRA_IMAGE_URI)?.let {
            selectedImageUri = Uri.parse(it)
            loadAndDisplayImage()
        }



        // Example to simulate updating freshness
        resultViewModel.updateFreshness("100%")


        setupObservers()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupObservers() {
        resultViewModel.fruitFreshness.observe(viewLifecycleOwner) { freshness ->
            Log.d("ResultFragment", "Freshness observed: $freshness")
            binding.fruitFreshnesText.text = freshness
            val freshnessValue = freshness.removeSuffix("%").toIntOrNull() ?: 0
            if (freshnessValue < 50) {
                binding.fruitFreshnes.background = resources.getDrawable(R.drawable.percent_circle_red_bg)
                binding.fruitFreshnesText.setTextColor(Color.RED)
            } else {
                binding.fruitFreshnes.background = resources.getDrawable(R.drawable.percent_circle_green_bg)
                binding.fruitFreshnesText.setTextColor(Color.GREEN)
            }
        }
    }

    private fun loadAndDisplayImage() {
        selectedImageUri?.let {
            Glide.with(this).load(it).into(binding.fruitImg)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_IMAGE_URI = "imageUri"
    }

}