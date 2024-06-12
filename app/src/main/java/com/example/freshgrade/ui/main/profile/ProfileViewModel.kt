package com.example.freshgrade.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshgrade.data.pref.UserModel
import com.example.freshgrade.data.repo.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {

            repository.logout()
        }
    }

    fun getUser(): LiveData<UserModel> {
        return repository.getUserData()
    }

    fun deleteAccount() {
        viewModelScope.launch {
            repository.deleteAccount()
        }
    }



}