package com.silverorange.videoplayer.data.di

import com.silverorange.videoplayer.data.remote.repository.VideosRepositoryImpl
import com.silverorange.videoplayer.domain.repository.VideosRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsVideosResponse(videosRepositoryImpl: VideosRepositoryImpl):VideosRepository
}