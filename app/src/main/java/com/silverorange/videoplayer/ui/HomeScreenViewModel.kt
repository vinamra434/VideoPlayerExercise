package com.silverorange.videoplayer.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.silverorange.videoplayer.data.model.VideosResponse
import com.silverorange.videoplayer.domain.repository.VideosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


data class HomeState(val loading: Boolean = false)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val videosRepository: VideosRepository,
    val player: Player
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    init {
        initializePlayer()

        viewModelScope.launch {
            videosRepository
                .getVideos()
                .onStart {
                    _loading.value = true
                }
                .onCompletion {
                    delay(1000)
                    _loading.value = false
                }
                .collectLatest { response ->
                    try {
                        val format: DateFormat =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)

                        val videosResponseList: List<VideosResponse> =
                            response.sortedByDescending { format.parse(it.publishedAt) }

                        setVideosOnPlayer(
                            videosResponseList.map {
                                MediaItem
                                    .Builder()
                                    .setUri(it.hlsURL.toString())
                                    .setMediaMetadata(
                                        MediaMetadata.Builder().setTitle(it.title)
                                            .setArtist(it.author?.name)
                                            .setDescription(it.description)
                                            .build()
                                    )
                                    .build()
                            }
                        )
                    } catch (e: Exception) {
                        _loading.value = true
                        Log.d("TAG", "Exception in simpledateformat ${e.message}")
                    }
                }

        }

    }

    private fun initializePlayer() {
        player.prepare()
        player.playWhenReady = true
    }

    private fun setVideosOnPlayer(items: List<MediaItem>) {
        player.setMediaItems(items)
    }


    override fun onCleared() {
        super.onCleared()
        player.release()
    }

}