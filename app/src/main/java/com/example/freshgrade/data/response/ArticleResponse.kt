package com.example.freshgrade.data.response

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ArticleResponse(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: Date? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("uploadDate")
    val uploadDate: Date? = null,

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("thumbnailUrl")
    val thumbnailUrl: String? = null,

    @field:SerializedName("type")
    val type: String? = null
)
