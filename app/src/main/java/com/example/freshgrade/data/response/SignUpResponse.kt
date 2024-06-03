package com.example.freshgrade.data.response

import com.google.gson.annotations.SerializedName

data class SignUpResponse(

	@field:SerializedName("access_token")
	val accessToken: String,


)
