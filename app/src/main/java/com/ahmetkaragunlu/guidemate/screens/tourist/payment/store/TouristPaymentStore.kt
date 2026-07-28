package com.ahmetkaragunlu.guidemate.screens.tourist.payment.store

import com.ahmetkaragunlu.guidemate.screens.common.formatting.PLATFORM_CURRENCY_CODE
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.model.PaymentAttemptStatus
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.model.PaymentAttemptUiModel
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.model.PaymentPurpose
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class TouristPaymentStore
    @Inject
    constructor() {
        private val _attempts = MutableStateFlow<List<PaymentAttemptUiModel>>(emptyList())
        val attempts: StateFlow<List<PaymentAttemptUiModel>> = _attempts.asStateFlow()

        fun createAttempt(
            purpose: PaymentPurpose,
            amountMinor: Long,
            method: PaymentMethod,
            tourSessionId: String? = null,
            participantCount: Int? = null,
            savedCardId: String? = null,
        ): String {
            val paymentAttemptId = UUID.randomUUID().toString()
            val attempt =
                PaymentAttemptUiModel(
                    paymentAttemptId = paymentAttemptId,
                    purpose = purpose,
                    amountMinor = amountMinor,
                    currencyCode = PLATFORM_CURRENCY_CODE,
                    method = method,
                    status =
                        if (method == PaymentMethod.WALLET) {
                            PaymentAttemptStatus.VERIFYING
                        } else {
                            PaymentAttemptStatus.REDIRECTING
                    },
                    tourSessionId = tourSessionId,
                    participantCount = participantCount,
                    savedCardId = savedCardId,
                    createdAt = Instant.now(),
                )
            _attempts.update { current -> current + attempt }
            return paymentAttemptId
        }

        fun updateStatus(
            paymentAttemptId: String,
            status: PaymentAttemptStatus,
        ) {
            _attempts.update { current ->
                current.map { attempt ->
                    if (attempt.paymentAttemptId == paymentAttemptId) {
                        attempt.copy(status = status)
                    } else {
                        attempt
                    }
                }
            }
        }

        fun findAttempt(paymentAttemptId: String): PaymentAttemptUiModel? =
            attempts.value.firstOrNull { it.paymentAttemptId == paymentAttemptId }
    }
