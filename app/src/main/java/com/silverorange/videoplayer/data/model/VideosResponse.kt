package com.silverorange.videoplayer.data.model


import com.google.gson.annotations.SerializedName

data class VideosResponse(
    @SerializedName("author")
    val author: Author?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("fullURL")
    val fullURL: String?,
    @SerializedName("hlsURL")
    val hlsURL: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("publishedAt")
    val publishedAt: String?,
    @SerializedName("title")
    val title: String?
)