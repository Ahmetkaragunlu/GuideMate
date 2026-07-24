package com.ahmetkaragunlu.guidemate.di

import com.ahmetkaragunlu.guidemate.data.remote.places.GooglePlacesCitySearchService
import com.ahmetkaragunlu.guidemate.screens.common.selection.data.CitySearchService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SelectionModule {
    @Binds
    @Singleton
    abstract fun bindCitySearchService(
        implementation: GooglePlacesCitySearchService,
    ): CitySearchService
}
