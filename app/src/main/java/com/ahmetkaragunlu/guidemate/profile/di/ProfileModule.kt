package com.ahmetkaragunlu.guidemate.profile.di

import com.ahmetkaragunlu.guidemate.profile.data.remote.api.GuideProfileApi
import com.ahmetkaragunlu.guidemate.profile.data.repository.GuideProfileRepositoryImpl
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {
    @Binds
    @Singleton
    abstract fun bindGuideProfileRepository(
        implementation: GuideProfileRepositoryImpl,
    ): GuideProfileRepository

    companion object {
        @Provides
        @Singleton
        fun provideGuideProfileApi(retrofit: Retrofit): GuideProfileApi =
            retrofit.create(GuideProfileApi::class.java)
    }
}
