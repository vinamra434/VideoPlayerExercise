package com.silverorange.videoplayer.data.di

import com.silverorange.videoplayer.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    fun provideConvertorFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, factory: GsonConverterFactory): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(factory)
            .build()

}