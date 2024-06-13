package com.example.freshgrade.data.response

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(

	@field:SerializedName("status")
	val status: String? = null
)

data class ChangePasswordRequest(

	@field:SerializedName("currentPassword")
	val currentPassword: String,

	@field:SerializedName("confirmNewPassword")
	val confirmNewPassword: String,

	@field:SerializedName("newPassword")
	val newPassword: String

)
