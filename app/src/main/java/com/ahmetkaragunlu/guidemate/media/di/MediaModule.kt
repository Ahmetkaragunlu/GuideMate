package com.ahmetkaragunlu.guidemate.media.di

import com.ahmetkaragunlu.guidemate.media.data.remote.api.MediaApi
import com.ahmetkaragunlu.guidemate.media.data.repository.MediaRepositoryImpl
import com.ahmetkaragunlu.guidemate.media.data.multipart.ContentResolverMediaPartFactory
import com.ahmetkaragunlu.guidemate.media.data.multipart.MediaPartFactory
import com.ahmetkaragunlu.guidemate.media.domain.repository.MediaRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaModule {
    @Binds
    @Singleton
    abstract fun bindMediaRepository(implementation: MediaRepositoryImpl): MediaRepository

    @Binds
    @Singleton
    abstract fun bindMediaPartFactory(
        implementation: ContentResolverMediaPartFactory,
    ): MediaPartFactory

    companion object {
        @Provides
        @Singleton
        fun provideMediaApi(retrofit: Retrofit): MediaApi = retrofit.create(MediaApi::class.java)
    }
}
