package com.example.freshgrade.ui.changepass

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshgrade.data.repo.UserRepository
import com.example.freshgrade.data.api.Result
import com.example.freshgrade.data.response.ChangePasswordRequest
import com.example.freshgrade.data.response.GetUserResponse
import kotlinx.coroutines.launch

class ChangePasswordViewModel(private val repository: UserRepository) : ViewModel() {
    private val _changePasswordResult = MutableLiveData<Result<Unit>>()
    val changePasswordResult: LiveData<Result<Unit>> = _changePasswordResult

    private val _userResponse = MutableLiveData<GetUserResponse>()
    val userResponse: LiveData<GetUserResponse> = _userResponse

    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _changePasswordResult.value = Result.Loading
            try {
                val request = ChangePasswordRequest(currentPassword, newPassword, confirmPassword)
                repository.changePassword(request)
                _changePasswordResult.value = Result.Success(Unit)
            } catch (e: Exception) {
                _changePasswordResult.value = Result.Error(e.message ?: "Failed to change password")
            }
        }
    }

    fun getUserData() {
        viewModelScope.launch {
            try {
                val response = repository.getUser()
                _userResponse.value = response
            } catch (e: Exception) {
                // Handle error fetching user data
                Log.e("ProfileViewModel", "getUserData: ${e.message}")
            }
        }
    }
}