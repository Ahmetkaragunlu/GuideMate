package com.ahmetkaragunlu.guidemate.di

import com.ahmetkaragunlu.guidemate.BuildConfig
import com.ahmetkaragunlu.guidemate.auth.data.remote.session.AuthInterceptor
import com.ahmetkaragunlu.guidemate.auth.data.remote.session.TokenAuthenticator
import com.ahmetkaragunlu.guidemate.common.network.ApiBaseUrl
import com.ahmetkaragunlu.guidemate.common.network.realtime.OkHttpRealtimeClient
import com.ahmetkaragunlu.guidemate.common.network.realtime.RealtimeClient
import com.ahmetkaragunlu.guidemate.common.network.serialization.InstantTypeAdapter
import com.ahmetkaragunlu.guidemate.common.network.serialization.LocalDateTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRealtimeClient(implementation: OkHttpRealtimeClient): RealtimeClient =
        implementation

    @Provides
    @Singleton
    @ApiBaseUrl
    fun provideApiBaseUrl(): HttpUrl = BuildConfig.GUIDEMATE_API_BASE_URL.toHttpUrl()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BASIC
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
        }

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .registerTypeAdapter(Instant::class.java, InstantTypeAdapter.nullSafe())
            .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter.nullSafe())
            .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(tokenAuthenticator)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
        @ApiBaseUrl apiBaseUrl: HttpUrl,
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(apiBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

}
