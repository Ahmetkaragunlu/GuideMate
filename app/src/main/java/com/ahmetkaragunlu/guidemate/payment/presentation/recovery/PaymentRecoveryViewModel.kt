package com.ahmetkaragunlu.guidemate.payment.presentation.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PaymentRecoveryViewModel @Inject constructor(
    paymentRepository: PaymentRepository,
) : ViewModel() {
    private val mutablePendingPaymentId = MutableStateFlow<String?>(null)
    val pendingPaymentId: StateFlow<String?> = mutablePendingPaymentId.asStateFlow()

    init {
        viewModelScope.launch {
            paymentRepository.pendingPaymentId.collect { paymentId ->
                mutablePendingPaymentId.value = paymentId
            }
        }
    }

    fun onRecoveryNavigationHandled() {
        mutablePendingPaymentId.value = null
    }
}
