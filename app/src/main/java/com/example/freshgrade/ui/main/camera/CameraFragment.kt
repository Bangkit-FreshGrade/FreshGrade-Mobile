package com.example.freshgrade.ui.main.camera

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.freshgrade.R
import com.example.freshgrade.adapter.ImgCamAdapter
import com.example.freshgrade.databinding.FragmentCameraBinding
import com.example.freshgrade.ui.decoration.CarouselItemDecoration

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private val cameraViewModel: CameraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

        // toggle between IV and TV in camera fragment
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
    }

}