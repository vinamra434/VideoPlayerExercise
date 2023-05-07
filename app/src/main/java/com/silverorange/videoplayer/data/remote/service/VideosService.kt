package com.silverorange.videoplayer.data.remote.service

import com.silverorange.videoplayer.data.model.VideosResponse
import com.silverorange.videoplayer.data.remote.BaseService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VideosService @Inject constructor() : BaseService() {

    suspend fun getVideos(): Flow<List<VideosResponse>> = safeApiCallFlow {
        api.getVideos()
    }

}