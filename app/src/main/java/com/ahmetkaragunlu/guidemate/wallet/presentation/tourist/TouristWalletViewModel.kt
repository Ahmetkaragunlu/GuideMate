package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.formatting.isValidCurrencyInput
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrencies
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import com.ahmetkaragunlu.guidemate.payment.domain.repository.SavedPaymentMethodRepository
import com.ahmetkaragunlu.guidemate.payment.presentation.locale.currentCheckoutLocale
import com.ahmetkaragunlu.guidemate.payment.presentation.mapper.toUiModel
import com.ahmetkaragunlu.guidemate.payment.presentation.model.PaymentLaunch
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import com.ahmetkaragunlu.guidemate.wallet.presentation.mapper.toTouristUiModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.model.TouristWalletUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.util.Currency
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val WALLET_PREVIEW_SIZE = 3

@HiltViewModel
class TouristWalletViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
        private val walletRepository: WalletRepository,
        private val savedPaymentMethodRepository: SavedPaymentMethodRepository,
        private val paymentRepository: PaymentRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow(TouristWalletUiState())
        val uiState: StateFlow<TouristWalletUiState> = mutableUiState.asStateFlow()
        private var paymentJob: Job? = null
        private var topUpIdempotencyKey: String?
            get() = savedStateHandle[TOP_UP_IDEMPOTENCY_KEY]
            set(value) {
                savedStateHandle[TOP_UP_IDEMPOTENCY_KEY] = value
            }

        init {
            refresh()
            viewModelScope.launch {
                savedPaymentMethodRepository.paymentMethodChanges.collect { refreshCards() }
            }
        }

        fun refresh() {
            viewModelScope.launch {
                mutableUiState.update { it.copy(loadState = ContentLoadState.LOADING) }
                val wallet = walletRepository.getWallet()
                val transactions =
                    walletRepository.getTransactions(page = 0, size = WALLET_PREVIEW_SIZE)
                val currencies = paymentRepository.getCheckoutCurrencies()
                if (
                    wallet !is DataResult.Success ||
                        transactions !is DataResult.Success ||
                        currencies !is DataResult.Success
                ) {
                    showLoadError()
                    return@launch
                }

                mutableUiState.update { current ->
                    current.copy(
                        loadState = ContentLoadState.CONTENT,
                        balanceMinor = wallet.data.balanceMinor,
                        currencyCode = wallet.data.currencyCode,
                        transactions =
                            transactions.data.items.mapNotNull { it.toTouristUiModel() },
                        chargeCurrencies = currencies.data.chargeCurrencies,
                        selectedChargeCurrencyCode =
                            current.selectedChargeCurrencyCode
                                ?.takeIf { code ->
                                    currencies.data.chargeCurrencies.any {
                                        it.currencyCode == code
                                    }
                                } ?: currencies.data.preferredCurrencyCode(),
                    )
                }
                refreshCards()
            }
        }

        fun onTopUpAmountChange(value: String) {
            if (value.isValidCurrencyInput()) {
                mutableUiState.update {
                    it.copy(
                        topUpAmount = value,
                        topUpQuote = null,
                        paymentActionError = null,
                    )
                }
                topUpIdempotencyKey = null
            }
        }

        fun onTopUpPresetSelected(amount: Int) {
            mutableUiState.update {
                it.copy(
                    topUpAmount = amount.toString(),
                    topUpQuote = null,
                    paymentActionError = null,
                )
            }
            topUpIdempotencyKey = null
        }

        fun onChargeCurrencySelected(currencyCode: String) {
            if (currencyCode == uiState.value.selectedChargeCurrencyCode) return
            mutableUiState.update {
                it.copy(
                    selectedChargeCurrencyCode = currencyCode,
                    topUpQuote = null,
                    paymentActionError = null,
                )
            }
            topUpIdempotencyKey = null
        }

        fun continueTopUp(amountMinor: Long) {
            if (amountMinor <= 0 || paymentJob?.isActive == true) return
            val state = uiState.value
            val currencyCode = state.selectedChargeCurrencyCode ?: return
            paymentJob =
                viewModelScope.launch {
                    mutableUiState.update {
                        it.copy(isPaymentActionInProgress = true, paymentActionError = null)
                    }
                    val quote = state.topUpQuote
                    if (
                        quote == null ||
                            quote.baseAmountMinor != amountMinor ||
                            quote.isExpired(Instant.now())
                    ) {
                        requestQuote(amountMinor, currencyCode)
                        return@launch
                    }
                    initializeTopUp(quote.id)
                }
        }

        fun onPaymentNavigationHandled() {
            mutableUiState.update {
                it.copy(
                    paymentLaunch = null,
                    topUpAmount = "",
                    topUpQuote = null,
                )
            }
        }

        private suspend fun requestQuote(
            amountMinor: Long,
            currencyCode: String,
        ) {
            when (
                val result =
                    paymentRepository.quoteWalletTopUp(
                        amountMinor = amountMinor,
                        chargeCurrencyCode = currencyCode,
                    )
            ) {
                is DataResult.Success ->
                    mutableUiState.update {
                        it.copy(
                            topUpQuote = result.data,
                            isPaymentActionInProgress = false,
                        )
                    }
                is DataResult.Error -> showPaymentError(result)
            }
        }

        private suspend fun initializeTopUp(quoteId: String) {
            val idempotencyKey =
                topUpIdempotencyKey ?: UUID.randomUUID().toString().also {
                    topUpIdempotencyKey = it
                }
            when (
                val result =
                    paymentRepository.checkoutWalletTopUp(
                        quoteId = quoteId,
                        locale = currentCheckoutLocale(),
                        idempotencyKey = idempotencyKey,
                    )
            ) {
                is DataResult.Success -> {
                    topUpIdempotencyKey = null
                    mutableUiState.update {
                        it.copy(
                            isPaymentActionInProgress = false,
                            paymentLaunch =
                                PaymentLaunch(
                                    paymentId = result.data.id,
                                    requiresHostedCheckout =
                                        result.data.method == PaymentMethod.HOSTED_CARD,
                                ),
                        )
                    }
                }
                is DataResult.Error -> showPaymentError(result)
            }
        }

        private suspend fun refreshCards() {
            when (val result = savedPaymentMethodRepository.getSavedPaymentMethods()) {
                is DataResult.Success ->
                    mutableUiState.update { current ->
                        current.copy(
                            savedCards =
                                result.data.map { it.toUiModel() }.sortedByDescending {
                                    it.isDefault
                                },
                        )
                    }
                is DataResult.Error ->
                    if (mutableUiState.value.savedCards.isEmpty()) showLoadError()
            }
        }

        private fun showPaymentError(error: DataResult.Error) {
            mutableUiState.update {
                it.copy(
                    isPaymentActionInProgress = false,
                    paymentActionError = error.error.toMessage(resourceProvider),
                )
            }
        }

        private fun showLoadError() {
            mutableUiState.update { it.copy(loadState = ContentLoadState.ERROR) }
        }

        private fun CheckoutCurrencies.preferredCurrencyCode(): String? {
            val deviceCurrencyCode =
                runCatching { Currency.getInstance(Locale.getDefault()).currencyCode }.getOrNull()
            return chargeCurrencies.firstOrNull { it.currencyCode == deviceCurrencyCode }?.currencyCode
                ?: chargeCurrencies.firstOrNull { it.currencyCode == baseCurrencyCode }?.currencyCode
                ?: chargeCurrencies.firstOrNull()?.currencyCode
        }

        private companion object {
            const val TOP_UP_IDEMPOTENCY_KEY = "top_up_idempotency_key"
        }
    }
