package com.example.freshgrade.data.api

import com.example.freshgrade.data.response.ArticleResponse
import com.example.freshgrade.data.response.ChangePasswordRequest
import com.example.freshgrade.data.response.ChangePasswordResponse
import com.example.freshgrade.data.response.GetUserResponse
import com.example.freshgrade.data.response.ScanResponse
import com.example.freshgrade.data.response.SignInRequest
import com.example.freshgrade.data.response.SignInResponse
import com.example.freshgrade.data.response.SignUpRequest
import com.example.freshgrade.data.response.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Headers("content-type: application/json")
    @POST("api/signup")
    suspend fun postSignup(@Body requestBody: SignUpRequest): SignUpResponse



    @Headers("content-type: application/json")
    @POST("api/signin")
    suspend fun postSignin(@Body requestBody: SignInRequest): SignInResponse



    @Headers("content-type: application/json")
    @GET("api/user")
    suspend fun getUser(
        @Header("Authorization") token: String,
    ): GetUserResponse

    @Headers("content-type: application/json")
    @PATCH("api/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body requestBody: ChangePasswordRequest
    ): ChangePasswordResponse

    @GET("api/articles")
    fun getArticles(): Call<List<ArticleResponse>>


    @Multipart
    @POST("api/predict")
    fun uploadImage(@Part image: MultipartBody.Part): Call<ScanResponse>

}