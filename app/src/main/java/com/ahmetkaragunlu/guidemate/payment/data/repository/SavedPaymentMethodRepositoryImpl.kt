package com.ahmetkaragunlu.guidemate.payment.data.repository

import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.payment.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.payment.data.remote.api.SavedPaymentMethodApi
import com.ahmetkaragunlu.guidemate.payment.domain.model.SavedPaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.repository.SavedPaymentMethodRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SavedPaymentMethodRepositoryImpl @Inject constructor(
    private val api: SavedPaymentMethodApi,
    private val apiCallExecutor: ApiCallExecutor,
) : SavedPaymentMethodRepository {
    private val mutablePaymentMethodChanges = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val paymentMethodChanges: Flow<Unit> = mutablePaymentMethodChanges.asSharedFlow()

    override suspend fun getSavedPaymentMethods(): DataResult<List<SavedPaymentMethod>> =
        apiCallExecutor.execute(
            request = api::getCards,
            transform = { cards -> cards.map { it.toDomain() } },
        )

    override suspend fun makeDefault(
        savedPaymentMethodId: String,
    ): DataResult<SavedPaymentMethod> =
        apiCallExecutor.execute(
            request = { api.makeDefault(savedPaymentMethodId) },
            transform = { it.toDomain() },
        ).also(::emitChangeOnSuccess)

    override suspend fun delete(savedPaymentMethodId: String): DataResult<Unit> =
        apiCallExecutor.executeUnit { api.delete(savedPaymentMethodId) }
            .also(::emitChangeOnSuccess)

    private fun emitChangeOnSuccess(result: DataResult<*>) {
        if (result is DataResult.Success) mutablePaymentMethodChanges.tryEmit(Unit)
    }
}
