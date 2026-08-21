package com.ahmetkaragunlu.guidemate.common.ui.resource.di

import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ResourceModule {
    @Binds
    @Singleton
    abstract fun bindResourceProvider(resourceProviderImpl: ResourceProviderImpl): ResourceProvider
}
