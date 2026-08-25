package com.ahmetkaragunlu.guidemate.auth.di

import com.ahmetkaragunlu.guidemate.auth.data.local.session.AndroidKeystoreSessionStorage
import com.ahmetkaragunlu.guidemate.auth.data.local.session.SecureSessionStorage
import com.ahmetkaragunlu.guidemate.auth.data.local.session.TokenManager
import com.ahmetkaragunlu.guidemate.auth.domain.session.AccessTokenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthStorageModule {
    @Binds
    @Singleton
    abstract fun bindSecureSessionStorage(
        storage: AndroidKeystoreSessionStorage,
    ): SecureSessionStorage

    @Binds
    abstract fun bindAccessTokenProvider(tokenManager: TokenManager): AccessTokenProvider
}
