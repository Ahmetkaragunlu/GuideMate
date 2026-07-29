package com.ahmetkaragunlu.guidemate.di

import com.ahmetkaragunlu.guidemate.data.local.AndroidKeystoreSecureStringStorage
import com.ahmetkaragunlu.guidemate.data.local.SecureStringStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {
    @Binds
    @Singleton
    abstract fun bindSecureStringStorage(
        storage: AndroidKeystoreSecureStringStorage,
    ): SecureStringStorage
}
