package com.example.freshgrade.data.response

import com.google.gson.annotations.SerializedName

data class SignUpResponse(

	@field:SerializedName("access_token")
	val accessToken: String
)

data class SignUpRequest(

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("firstName")
	val firstName: String,

	@field:SerializedName("lastName")
	val lastName: String


)
