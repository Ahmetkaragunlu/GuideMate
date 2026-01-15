package com.ahmetkaragunlu.guidemate.di


import com.ahmetkaragunlu.guidemate.data.repository.AuthRepositoryImpl
import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}