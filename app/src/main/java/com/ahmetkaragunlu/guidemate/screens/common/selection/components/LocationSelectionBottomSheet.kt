package com.ahmetkaragunlu.guidemate.screens.common.selection.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import com.ahmetkaragunlu.guidemate.screens.common.selection.data.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.LocationOption
import java.util.Locale

@Composable
fun LocationSelectionBottomSheet(
    isVisible: Boolean,
    selectedCountryCode: String?,
    onDismissRequest: () -> Unit,
    onLocationSelected: (LocationOption) -> Unit,
) {
    var step by rememberSaveable { mutableStateOf(LocationSelectionStep.COUNTRY) }
    var pendingCountryCode by rememberSaveable { mutableStateOf(selectedCountryCode) }
    var wasVisible by rememberSaveable { mutableStateOf(false) }
    val locale = LocalConfiguration.current.locales[0] ?: Locale.getDefault()
    val selectedCountry = remember(pendingCountryCode, locale.toLanguageTag()) {
        pendingCountryCode?.let { LocaleSelectionCatalog.country(it, locale) }
    }

    LaunchedEffect(isVisible, selectedCountryCode) {
        if (isVisible && !wasVisible) {
            step = LocationSelectionStep.COUNTRY
            pendingCountryCode = selectedCountryCode
        }
        wasVisible = isVisible
    }

    CountrySelectionBottomSheet(
        isVisible = isVisible && step == LocationSelectionStep.COUNTRY,
        selectedCountryCode = selectedCountry?.code,
        onDismissRequest = onDismissRequest,
        onCountrySelected = { country ->
            pendingCountryCode = country.code
            step = LocationSelectionStep.CITY
        },
    )

    selectedCountry?.let { country ->
        CitySelectionBottomSheet(
            isVisible = isVisible && step == LocationSelectionStep.CITY,
            country = country,
            onDismissRequest = onDismissRequest,
            onCitySelected = { city ->
                onLocationSelected(LocationOption(country = country, city = city))
            },
        )
    }
}

private enum class LocationSelectionStep {
    COUNTRY, CITY,
}
