package com.ahmetkaragunlu.guidemate.payment.di

import com.ahmetkaragunlu.guidemate.payment.data.remote.api.SavedPaymentMethodApi
import com.ahmetkaragunlu.guidemate.payment.data.repository.SavedPaymentMethodRepositoryImpl
import com.ahmetkaragunlu.guidemate.payment.domain.repository.SavedPaymentMethodRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class PaymentModule {
    @Binds
    @Singleton
    abstract fun bindSavedPaymentMethodRepository(
        implementation: SavedPaymentMethodRepositoryImpl,
    ): SavedPaymentMethodRepository

    companion object {
        @Provides
        @Singleton
        fun provideSavedPaymentMethodApi(retrofit: Retrofit): SavedPaymentMethodApi =
            retrofit.create(SavedPaymentMethodApi::class.java)
    }
}
