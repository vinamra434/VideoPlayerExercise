package com.silverorange.videoplayer.domain.repository

import com.silverorange.videoplayer.data.model.VideosResponse
import kotlinx.coroutines.flow.Flow


interface VideosRepository {

    suspend fun getVideos(): Flow<List<VideosResponse>>
}