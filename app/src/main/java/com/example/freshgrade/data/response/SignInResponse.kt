package com.example.freshgrade.data.response

import com.google.gson.annotations.SerializedName

data class SignInResponse(
	@field:SerializedName("loginResult")
	val loginResult: SignInResult? = null,

	@field:SerializedName("access_token")
	val accessToken: String
)

data class SignInResult(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class SignInRequest(

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("password")
	val password: String

)
