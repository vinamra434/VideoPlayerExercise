package com.silverorange.videoplayer.data.remote

import com.silverorange.videoplayer.data.model.RequestException
import com.silverorange.videoplayer.data.remote.service.VideosAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

abstract class BaseService {

    @Inject
    lateinit var api: VideosAPI

    suspend fun <T> safeApiCallFlow(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> T
    ) = flow {
        emit(apiCall())
    }.flowOn(dispatcher)
        .catch { exception ->
            handleException(exception)
        }

    private fun handleException(throwable: Throwable) {
        throwable.printStackTrace()
        val exception = when (throwable) {
            is HttpException -> RequestException(
                throwable.code(),
                throwable.response()?.errorBody()?.string() ?: "Unknown error"
            )
            else -> throwable
        }

        throw exception
    }
}