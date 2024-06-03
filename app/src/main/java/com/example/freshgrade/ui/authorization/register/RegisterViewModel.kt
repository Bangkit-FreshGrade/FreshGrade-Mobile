package com.example.freshgrade.ui.authorization.register

import androidx.lifecycle.ViewModel
import com.example.freshgrade.data.repo.UserRepository

class RegisterViewModel (private val userRepository: UserRepository) : ViewModel() {
//    private val _responeRegister = MutableLiveData<Result<RegisterResponse>>()

    fun register( email: String, password: String, userName: String, firstName: String, lastName: String ) =
        userRepository.register( email, password, userName, firstName, lastName )
}