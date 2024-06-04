package com.example.freshgrade.ui.main.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.freshgrade.R
import com.example.freshgrade.data.carousel_item.ImgCamera

class CameraViewModel : ViewModel() {
    private val _items = MutableLiveData<List<ImgCamera>>()
    val items: LiveData<List<ImgCamera>> get() = _items

    init {
        loadItems()
    }

    private fun loadItems() {
        // Sample data, this could be loaded from a repository or database
        val sampleItems = listOf(
            ImgCamera(R.drawable.car1),
            ImgCamera(R.drawable.car2),
            ImgCamera(R.drawable.car3),
            ImgCamera(R.drawable.car4),
            ImgCamera(R.drawable.car5),
            ImgCamera(R.drawable.car6)
        )
        _items.value = sampleItems
    }
}