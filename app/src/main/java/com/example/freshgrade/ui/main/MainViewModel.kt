package com.example.freshgrade.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.freshgrade.data.pref.UserModel
import com.example.freshgrade.data.repo.UserRepository

class MainViewModel(private val repository: UserRepository) : ViewModel() {



    fun getUser(): LiveData<UserModel> {
        return repository.getUserData()
    }

}