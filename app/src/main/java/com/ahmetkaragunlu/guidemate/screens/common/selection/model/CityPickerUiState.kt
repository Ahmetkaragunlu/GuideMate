package com.ahmetkaragunlu.guidemate.screens.common.selection.model

data class CityPickerUiState(
    val countryCode: String = "",
    val query: String = "",
    val results: List<CitySearchResult> = emptyList(),
    val selectedCity: CityOption? = null,
    val isLoading: Boolean = false,
    val isResolvingSelection: Boolean = false,
    val hasError: Boolean = false,
)
