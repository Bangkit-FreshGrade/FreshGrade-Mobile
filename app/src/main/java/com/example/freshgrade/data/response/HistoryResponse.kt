package com.example.freshgrade.data.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("HistoryResponse")
	val historyResponse: List<HistoryResponseItem>
)

data class HistoryResponseItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("disease")
	val disease: String,

	@field:SerializedName("fruit")
	val fruit: String,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("value")
	val value: Double,

	@field:SerializedName("createdById")
	val createdById: String
)
