package com.example.freshgrade.ui.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshgrade.data.repo.UserRepository
import com.example.freshgrade.data.response.GetUserResponse
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    private val _userResponse = MutableLiveData<GetUserResponse>()
    val userResponse: LiveData<GetUserResponse> = _userResponse

    fun getUserData() {
        viewModelScope.launch {
            try {
                val response = repository.getUser()
                _userResponse.value = response
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "getUserData: ${e.message}")
            }
        }
    }


        fun logout() {
        viewModelScope.launch {

            repository.logout()
        }
    }





}