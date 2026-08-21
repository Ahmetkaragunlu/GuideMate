package com.ahmetkaragunlu.guidemate.auth.di

import com.ahmetkaragunlu.guidemate.auth.data.repository.AuthRepositoryImpl
import com.ahmetkaragunlu.guidemate.auth.data.repository.OnboardingRepositoryImpl
import com.ahmetkaragunlu.guidemate.auth.data.repository.UserRepositoryImpl
import com.ahmetkaragunlu.guidemate.auth.domain.repository.AuthRepository
import com.ahmetkaragunlu.guidemate.auth.domain.repository.OnboardingRepository
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(
        onboardingRepositoryImpl: OnboardingRepositoryImpl,
    ): OnboardingRepository
}
