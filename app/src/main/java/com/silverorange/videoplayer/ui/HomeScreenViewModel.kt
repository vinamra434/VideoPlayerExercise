package com.silverorange.videoplayer.ui

import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.Player
import com.silverorange.videoplayer.domain.repository.VideosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    }

    private fun initializePlayer() {
        player.prepare()
        player.playWhenReady = true
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

}