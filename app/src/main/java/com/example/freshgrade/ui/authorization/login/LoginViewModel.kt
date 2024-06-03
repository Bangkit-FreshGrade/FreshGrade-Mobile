package com.example.freshgrade.ui.authorization.login

import androidx.lifecycle.ViewModel
import com.example.freshgrade.data.repo.UserRepository

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun login(email: String, password: String) = userRepository.userLogin(email, password)
}