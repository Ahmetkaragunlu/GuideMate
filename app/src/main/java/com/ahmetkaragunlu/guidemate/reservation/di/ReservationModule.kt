package com.ahmetkaragunlu.guidemate.reservation.di

import com.ahmetkaragunlu.guidemate.reservation.data.remote.api.ReservationApi
import com.ahmetkaragunlu.guidemate.reservation.data.repository.ReservationRepositoryImpl
import com.ahmetkaragunlu.guidemate.reservation.domain.repository.ReservationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class ReservationModule {
    @Binds
    @Singleton
    abstract fun bindReservationRepository(
        implementation: ReservationRepositoryImpl,
    ): ReservationRepository

    companion object {
        @Provides
        @Singleton
        fun provideReservationApi(retrofit: Retrofit): ReservationApi =
            retrofit.create(ReservationApi::class.java)
    }
}
