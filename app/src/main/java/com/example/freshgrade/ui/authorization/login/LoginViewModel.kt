package com.example.freshgrade.ui.authorization.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshgrade.data.pref.UserModel
import com.example.freshgrade.data.repo.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun userLogin(email: String, password: String) =
        userRepository.userLogin(email, password)//

    fun saveUserData(userData: UserModel) {
        viewModelScope.launch {
            userRepository.saveUserData(userData)
        }
    }

    fun login() {
        viewModelScope.launch {
            userRepository.signIn()
        }
    }}