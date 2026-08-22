package com.ahmetkaragunlu.guidemate.wallet.di

import com.ahmetkaragunlu.guidemate.wallet.data.remote.api.WalletApi
import com.ahmetkaragunlu.guidemate.wallet.data.repository.WalletRepositoryImpl
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class WalletModule {
    @Binds
    @Singleton
    abstract fun bindWalletRepository(
        implementation: WalletRepositoryImpl,
    ): WalletRepository

    companion object {
        @Provides
        @Singleton
        fun provideWalletApi(retrofit: Retrofit): WalletApi =
            retrofit.create(WalletApi::class.java)
    }
}
