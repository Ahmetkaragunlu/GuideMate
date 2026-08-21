package com.ahmetkaragunlu.guidemate.common.location.di

import com.ahmetkaragunlu.guidemate.common.location.data.remote.GooglePlacesCitySearchService
import com.ahmetkaragunlu.guidemate.common.location.data.CitySearchService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {
    @Binds
    @Singleton
    abstract fun bindCitySearchService(
        implementation: GooglePlacesCitySearchService,
    ): CitySearchService
}
