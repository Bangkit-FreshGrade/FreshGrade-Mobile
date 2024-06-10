package com.example.freshgrade.ui.main.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel() {
    private val _fruitFreshness = MutableLiveData<String>()
    val fruitFreshness: LiveData<String> get() = _fruitFreshness

    // Function to update the freshness value
    fun updateFreshness(freshness: String) {
        _fruitFreshness.value = freshness
    }
}