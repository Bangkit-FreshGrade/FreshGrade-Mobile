package com.example.freshgrade.data.api

import com.example.freshgrade.data.response.ChangePasswordRequest
import com.example.freshgrade.data.response.ChangePasswordResponse
import com.example.freshgrade.data.response.GetUserResponse
import com.example.freshgrade.data.response.SignInRequest
import com.example.freshgrade.data.response.SignInResponse
import com.example.freshgrade.data.response.SignUpRequest
import com.example.freshgrade.data.response.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ApiService {
    @Headers("content-type: application/json")
    @POST("api/signup")
    suspend fun postSignup(@Body requestBody: SignUpRequest): SignUpResponse
    @FormUrlEncoded
    @Headers("content-type: application/json")
    @POST("api/signup")
    suspend fun postSignUp(
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): SignUpResponse


    @Headers("content-type: application/json")
    @POST("api/signin")
    suspend fun postSignin(@Body requestBody: SignInRequest): SignInResponse

//    @Headers("content-type: application/json")
//    @POST("api/signin")
//    suspend fun postSignIn(
//        @Field("email") email: String,
//        @Field("password") password: String,
//    ): SignInResponse

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


}