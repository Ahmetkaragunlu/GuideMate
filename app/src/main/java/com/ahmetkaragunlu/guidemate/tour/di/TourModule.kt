package com.ahmetkaragunlu.guidemate.tour.di

import com.ahmetkaragunlu.guidemate.tour.data.remote.api.GuideTourApi
import com.ahmetkaragunlu.guidemate.tour.data.repository.GuideTourRepositoryImpl
import com.ahmetkaragunlu.guidemate.tour.domain.repository.GuideTourRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class TourModule {
    @Binds
    @Singleton
    abstract fun bindGuideTourRepository(
        implementation: GuideTourRepositoryImpl,
    ): GuideTourRepository

    companion object {
        @Provides
        @Singleton
        fun provideGuideTourApi(retrofit: Retrofit): GuideTourApi =
            retrofit.create(GuideTourApi::class.java)
    }
}
