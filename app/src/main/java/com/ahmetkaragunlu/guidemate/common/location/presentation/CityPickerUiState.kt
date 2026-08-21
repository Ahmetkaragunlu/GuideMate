package com.ahmetkaragunlu.guidemate.common.location.presentation

import com.ahmetkaragunlu.guidemate.common.location.model.CityOption
import com.ahmetkaragunlu.guidemate.common.location.model.CitySearchResult

data class CityPickerUiState(
    val countryCode: String = "",
    val query: String = "",
    val results: List<CitySearchResult> = emptyList(),
    val selectedCity: CityOption? = null,
    val isLoading: Boolean = false,
    val isResolvingSelection: Boolean = false,
    val hasError: Boolean = false,
)
