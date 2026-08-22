package com.ahmetkaragunlu.guidemate.payment.domain.repository

import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.payment.domain.model.SavedPaymentMethod
import kotlinx.coroutines.flow.Flow

interface SavedPaymentMethodRepository {
    val paymentMethodChanges: Flow<Unit>

    suspend fun getSavedPaymentMethods(): DataResult<List<SavedPaymentMethod>>

    suspend fun makeDefault(savedPaymentMethodId: String): DataResult<SavedPaymentMethod>

    suspend fun delete(savedPaymentMethodId: String): DataResult<Unit>
}
