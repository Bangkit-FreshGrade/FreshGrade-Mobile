package com.example.freshgrade.data.response

import com.google.gson.annotations.SerializedName

data class ScanResponse (
    @SerializedName("id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("fruit") val fruit: String,
    @SerializedName("value") val value: Double,
    @SerializedName("disease") val disease: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("desc") val desc: String?,
    @SerializedName("createdById") val createdById: String
)

