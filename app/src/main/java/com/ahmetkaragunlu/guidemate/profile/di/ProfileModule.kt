package com.ahmetkaragunlu.guidemate.profile.di

import com.ahmetkaragunlu.guidemate.profile.data.remote.api.GuideProfileApi
import com.ahmetkaragunlu.guidemate.profile.data.remote.api.UserAvatarApi
import com.ahmetkaragunlu.guidemate.profile.data.repository.GuideProfileRepositoryImpl
import com.ahmetkaragunlu.guidemate.profile.data.repository.UserAvatarRepositoryImpl
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.profile.domain.repository.UserAvatarRepository
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

    @Binds
    @Singleton
    abstract fun bindUserAvatarRepository(
        implementation: UserAvatarRepositoryImpl,
    ): UserAvatarRepository

    companion object {
        @Provides
        @Singleton
        fun provideGuideProfileApi(retrofit: Retrofit): GuideProfileApi =
            retrofit.create(GuideProfileApi::class.java)

        @Provides
        @Singleton
        fun provideUserAvatarApi(retrofit: Retrofit): UserAvatarApi =
            retrofit.create(UserAvatarApi::class.java)
    }
}
