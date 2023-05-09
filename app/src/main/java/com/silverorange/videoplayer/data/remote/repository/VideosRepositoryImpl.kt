package com.silverorange.videoplayer.data.remote.repository

import com.silverorange.videoplayer.data.model.VideosResponse
import com.silverorange.videoplayer.data.remote.service.VideosService
import com.silverorange.videoplayer.domain.repository.VideosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VideosRepositoryImpl @Inject constructor(
    private val service: VideosService
): VideosRepository {

    override suspend fun getVideos(): Flow<List<VideosResponse>>  =
        service.getVideos()

}