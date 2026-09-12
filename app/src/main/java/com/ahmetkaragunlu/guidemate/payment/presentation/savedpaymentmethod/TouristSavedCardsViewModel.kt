package com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.payment.domain.repository.SavedPaymentMethodRepository
import com.ahmetkaragunlu.guidemate.payment.presentation.mapper.toUiModel
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.model.SavedCardsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TouristSavedCardsViewModel
    @Inject
    constructor(
        private val repository: SavedPaymentMethodRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow(SavedCardsUiState())
        val uiState: StateFlow<SavedCardsUiState> = mutableUiState.asStateFlow()

        init {
            refresh()
        }

        fun refresh() {
            viewModelScope.launch {
                refreshSavedCards(showLoading = true)
            }
        }

        fun onShowDeleteDialog(cardId: String) {
            mutableUiState.update { it.copy(showDeleteDialogFor = cardId) }
        }

        fun onDismissDeleteDialog() {
            mutableUiState.update { it.copy(showDeleteDialogFor = null) }
        }

        fun onConfirmDeleteCard() {
            val cardId = mutableUiState.value.showDeleteDialogFor ?: return
            runMutation { repository.delete(cardId) }
        }

        fun onErrorShown() {
            mutableUiState.update { it.copy(errorMessage = null) }
        }

        private fun runMutation(block: suspend () -> DataResult<*>) {
            if (mutableUiState.value.isMutationInProgress) return
            viewModelScope.launch {
                mutableUiState.update {
                    it.copy(
                        isMutationInProgress = true,
                        showDeleteDialogFor = null,
                    )
                }
                try {
                    when (val result = block()) {
                        is DataResult.Success -> refreshSavedCards(showLoading = false)
                        is DataResult.Error -> showError(result)
                    }
                } finally {
                    mutableUiState.update { it.copy(isMutationInProgress = false) }
                }
            }
        }

        private suspend fun refreshSavedCards(showLoading: Boolean) {
            if (showLoading) {
                mutableUiState.update {
                    it.copy(loadState = ContentLoadState.LOADING, errorMessage = null)
                }
            }
            when (val result = repository.getSavedPaymentMethods()) {
                is DataResult.Success ->
                    mutableUiState.update { current ->
                        current.copy(
                            loadState = ContentLoadState.CONTENT,
                            savedCards = result.data.map { it.toUiModel() },
                            errorMessage = null,
                        )
                    }
                is DataResult.Error -> showError(result, isLoadError = true)
            }
        }

        private fun showError(
            result: DataResult.Error,
            isLoadError: Boolean = false,
        ) {
            mutableUiState.update {
                it.copy(
                    loadState = if (isLoadError) ContentLoadState.ERROR else it.loadState,
                    errorMessage = result.error.toMessage(resourceProvider),
                )
            }
        }
    }
