package com.example.freshgrade.data.api

import com.example.freshgrade.data.response.SignInRequest
import com.example.freshgrade.data.response.SignInResponse
import com.example.freshgrade.data.response.SignUpRequest
import com.example.freshgrade.data.response.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("content-type: application/json")
//    @FormUrlEncoded
    @POST("api/signup")
    suspend fun postSignup(@Body requestBody: SignUpRequest): SignUpResponse
    @Headers("content-type: application/json")
//    @FormUrlEncoded
    @POST("api/signin")
    suspend fun postSignin(@Body requestBody: SignInRequest): SignInResponse




}