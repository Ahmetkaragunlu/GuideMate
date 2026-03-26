package com.ahmetkaragunlu.guidemate.di

import com.ahmetkaragunlu.guidemate.data.repository.AuthRepositoryImpl
import com.ahmetkaragunlu.guidemate.data.repository.UserRepositoryImpl
import com.ahmetkaragunlu.guidemate.domain.AuthRepository
import com.ahmetkaragunlu.guidemate.domain.UserRepository
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
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}
