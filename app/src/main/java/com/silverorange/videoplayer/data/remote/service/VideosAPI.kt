package com.silverorange.videoplayer.data.remote.service

import com.silverorange.videoplayer.data.model.VideosResponse
import com.silverorange.videoplayer.data.remote.Endpoints
import retrofit2.http.GET

interface VideosAPI {

    @GET(Endpoints.VIDEOS)
    suspend fun getVideos(): List<VideosResponse>
}