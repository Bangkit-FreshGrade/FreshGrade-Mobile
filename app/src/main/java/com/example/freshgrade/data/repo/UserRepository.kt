package com.example.freshgrade.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.freshgrade.data.api.ApiService
import com.example.freshgrade.data.pref.UserPreference
import com.example.freshgrade.data.api.Result
import com.example.freshgrade.data.pref.UserModel
import com.example.freshgrade.data.response.SignInRequest
import com.example.freshgrade.data.response.SignInResponse
import com.example.freshgrade.data.response.SignUpRequest
import com.example.freshgrade.data.response.SignUpResponse
import kotlinx.coroutines.flow.first


class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference

) {

    suspend fun saveUserData(userEntity: UserModel) {
        userPreference.saveUserData(userEntity)
    }

    fun getUserData(): LiveData<UserModel> {
        return userPreference.getUserData().asLiveData()
    }

    suspend fun signIn() {
        userPreference.signIn()
    }


    suspend fun logout() {
        userPreference.logout()
        val userModel = userPreference.getUserData().first()

        val isNameEmpty = userModel.name.isEmpty()
        if (isNameEmpty) {
            Log.d("Logout", "User name successfully removed")
        } else {
            Log.e("Logout", "Error: User name not removed")
        }

        val isTokenEmpty = userModel.token.isEmpty()
        if (isTokenEmpty) {
            Log.d("Logout", "Token successfully removed")
        } else {
            Log.e("Logout", "Error: Token not removed")
        }

    }

    suspend fun deleteAccount() {
        userPreference.deleteAccount()
        Log.d("Logout", "User data successfully removed")
    }


    fun register(
        email: String,
        password: String,
        userName: String,
        firstName: String,
        lastName: String
    ): LiveData<Result<SignUpResponse>> = liveData {
        emit(Result.Loading)
        try {
            val request = SignUpRequest(email, password, userName, firstName, lastName)
            val response = apiService.postSignup(request)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("UserRepository", "register: ${e.message.toString()}")
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun userLogin(email: String, password: String): LiveData<Result<SignInResponse>> = liveData {
        emit(Result.Loading)
        try {
            val request = SignInRequest(email, password)
            val response = apiService.postSignin(request)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("LoginViewModel", "postLogin: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}