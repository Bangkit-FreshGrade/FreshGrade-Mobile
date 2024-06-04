package com.example.freshgrade.data.response

import com.google.gson.annotations.SerializedName

data class SignInRequest(

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("password")
	val password: String

)
