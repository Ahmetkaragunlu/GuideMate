package com.ahmetkaragunlu.guidemate.screens.common.selection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.screens.common.selection.data.CitySearchService
import com.ahmetkaragunlu.guidemate.screens.common.selection.data.CitySearchSession
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.CityPickerUiState
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.CitySearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CityPickerViewModel
    @Inject
    constructor(
        private val citySearchService: CitySearchService,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(CityPickerUiState())
        val uiState = _uiState.asStateFlow()

        private var session: CitySearchSession? = null
        private var searchJob: Job? = null

        fun start(countryCode: String) {
            searchJob?.cancel()
            session = citySearchService.createSession(countryCode)
            _uiState.value = CityPickerUiState(countryCode = countryCode)
        }

        fun onQueryChange(query: String) {
            _uiState.update {
                it.copy(
                    query = query,
                    results = if (query.length < MINIMUM_QUERY_LENGTH) emptyList() else it.results,
                    isLoading = false,
                    hasError = false,
                )
            }
            searchJob?.cancel()
            if (query.trim().length < MINIMUM_QUERY_LENGTH) return

            searchJob =
                viewModelScope.launch {
                    delay(SEARCH_DEBOUNCE_MILLIS)
                    search(query)
                }
        }

        fun retry() {
            val query = _uiState.value.query
            if (query.trim().length >= MINIMUM_QUERY_LENGTH) {
                searchJob?.cancel()
                searchJob = viewModelScope.launch { search(query) }
            }
        }

        fun onResultSelected(result: CitySearchResult) {
            val activeSession = session ?: return
            viewModelScope.launch {
                _uiState.update { it.copy(isResolvingSelection = true, hasError = false) }
                activeSession
                    .resolve(result)
                    .onSuccess { city ->
                        _uiState.update {
                            it.copy(selectedCity = city, isResolvingSelection = false)
                        }
                    }
                    .onFailure {
                        _uiState.update {
                            it.copy(isResolvingSelection = false, hasError = true)
                        }
                    }
            }
        }

        fun consumeSelection() {
            _uiState.update { it.copy(selectedCity = null) }
        }

        private suspend fun search(query: String) {
            val activeSession = session ?: return
            _uiState.update { it.copy(isLoading = true, hasError = false) }
            activeSession
                .search(query)
                .onSuccess { results ->
                    _uiState.update {
                        it.copy(
                            results = results,
                            isLoading = false,
                            hasError = false,
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            results = emptyList(),
                            isLoading = false,
                            hasError = true,
                        )
                    }
                }
        }

        private companion object {
            const val MINIMUM_QUERY_LENGTH = 2
            const val SEARCH_DEBOUNCE_MILLIS = 350L
        }
    }
