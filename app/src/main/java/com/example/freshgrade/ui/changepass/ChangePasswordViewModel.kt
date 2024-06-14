package com.example.freshgrade.ui.changepass

import androidx.lifecycle.ViewModel
import com.example.freshgrade.data.repo.UserRepository

class ChangePasswordViewModel(private val repository: UserRepository) : ViewModel() {
    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) =
        repository.changePassword(currentPassword, newPassword, confirmPassword)
}