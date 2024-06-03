package com.example.freshgrade.data.api

import com.example.freshgrade.data.response.SignInResponse
import com.example.freshgrade.data.response.SignUpResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("signup")
    suspend fun postSignup(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("userName") userName: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String
    ): SignUpResponse

    @FormUrlEncoded
    @POST("signin")
    suspend fun postSignin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): SignInResponse



}