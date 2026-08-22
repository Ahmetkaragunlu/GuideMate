package com.ahmetkaragunlu.guidemate.tour.di

import com.ahmetkaragunlu.guidemate.tour.data.remote.api.GuideTourApi
import com.ahmetkaragunlu.guidemate.tour.data.remote.api.TourDiscoveryApi
import com.ahmetkaragunlu.guidemate.tour.data.repository.GuideTourRepositoryImpl
import com.ahmetkaragunlu.guidemate.tour.data.repository.TourDiscoveryRepositoryImpl
import com.ahmetkaragunlu.guidemate.tour.domain.repository.GuideTourRepository
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
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

    @Binds
    @Singleton
    abstract fun bindTourDiscoveryRepository(
        implementation: TourDiscoveryRepositoryImpl,
    ): TourDiscoveryRepository

    companion object {
        @Provides
        @Singleton
        fun provideGuideTourApi(retrofit: Retrofit): GuideTourApi =
            retrofit.create(GuideTourApi::class.java)

        @Provides
        @Singleton
        fun provideTourDiscoveryApi(retrofit: Retrofit): TourDiscoveryApi =
            retrofit.create(TourDiscoveryApi::class.java)
    }
}
