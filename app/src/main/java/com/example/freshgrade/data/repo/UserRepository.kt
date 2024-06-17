package com.example.freshgrade.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.freshgrade.data.api.ApiService
import com.example.freshgrade.data.pref.UserPreference
import com.example.freshgrade.data.api.Result
import com.example.freshgrade.data.pref.UserModel
import com.example.freshgrade.data.response.ChangePasswordRequest
import com.example.freshgrade.data.response.ChangePasswordResponse
import com.example.freshgrade.data.response.GetUserResponse
import com.example.freshgrade.data.response.HistoryResponse
import com.example.freshgrade.data.response.HistoryResponseItem
import com.example.freshgrade.data.response.SignInRequest
import com.example.freshgrade.data.response.SignInResponse
import com.example.freshgrade.data.response.SignUpRequest
import com.example.freshgrade.data.response.SignUpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.Call


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

    fun getToken(): Flow<String?> {
        return userPreference.getToken()
    }

    fun getHistory(): Call<List<HistoryResponseItem>> {
        return apiService.getHistory()
    }

    suspend fun getUser(): GetUserResponse {
        val token = getToken().firstOrNull() ?: throw IllegalStateException("Token not available")
        return apiService.getUser("Bearer $token")
    }


    suspend fun signIn() {
        userPreference.signIn()
    }


    suspend fun logout() {
        userPreference.logout()
        val userModel = userPreference.getUserData().first()

        val isTokenEmpty = userModel.token.isEmpty()
        if (isTokenEmpty) {
            Log.d("Logout", "Token successfully removed")
        } else {
            Log.e("Logout", "Error: Token not removed")
        }
    }


    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): LiveData<Result<ChangePasswordResponse>> = liveData {
        val token = userPreference.getToken().firstOrNull()
            ?: throw IllegalStateException("Token not available")
        val request = ChangePasswordRequest(currentPassword, newPassword, confirmPassword)
        Log.d("UserRepository", "Token: $token")
        Log.d("UserRepository", "ChangePasswordRequest: $request")

        emit(Result.Loading)
        try {
            val response = apiService.changePassword("Bearer $token", request)
            Log.d("UserRepository", "ChangePasswordResponse: $response")
            Log.d("UserRepository", "ChangePasswordRequest: $request")
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("UserRepository", "ChangePasswordRequest: $request")
            Log.e("UserRepository", "ChangePassword failed: ${e.message}")
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
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