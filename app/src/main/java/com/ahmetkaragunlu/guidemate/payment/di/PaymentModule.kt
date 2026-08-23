package com.ahmetkaragunlu.guidemate.payment.di

import com.ahmetkaragunlu.guidemate.payment.data.local.DataStorePendingPaymentStorage
import com.ahmetkaragunlu.guidemate.payment.data.local.PendingPaymentStorage
import com.ahmetkaragunlu.guidemate.payment.data.remote.api.SavedPaymentMethodApi
import com.ahmetkaragunlu.guidemate.payment.data.remote.api.PaymentApi
import com.ahmetkaragunlu.guidemate.payment.data.repository.PaymentRepositoryImpl
import com.ahmetkaragunlu.guidemate.payment.data.repository.SavedPaymentMethodRepositoryImpl
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
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
    abstract fun bindPendingPaymentStorage(
        implementation: DataStorePendingPaymentStorage,
    ): PendingPaymentStorage

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        implementation: PaymentRepositoryImpl,
    ): PaymentRepository

    @Binds
    @Singleton
    abstract fun bindSavedPaymentMethodRepository(
        implementation: SavedPaymentMethodRepositoryImpl,
    ): SavedPaymentMethodRepository

    companion object {
        @Provides
        @Singleton
        fun providePaymentApi(retrofit: Retrofit): PaymentApi = retrofit.create(PaymentApi::class.java)

        @Provides
        @Singleton
        fun provideSavedPaymentMethodApi(retrofit: Retrofit): SavedPaymentMethodApi =
            retrofit.create(SavedPaymentMethodApi::class.java)
    }
}
