package com.silverorange.videoplayer.data.di

import android.app.Application
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object VideoPlayerModule {

    @Provides
    fun provideVideoPlayer(application: Application): Player = ExoPlayer
        .Builder(application)
        .build()
}