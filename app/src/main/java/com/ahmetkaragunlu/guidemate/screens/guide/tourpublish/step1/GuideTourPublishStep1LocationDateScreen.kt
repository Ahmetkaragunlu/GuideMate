package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step1

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.screens.common.selection.components.LocationSelectionBottomSheet
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.LocationOption
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step1.content.GuideTourPublishStep1LocationDateContent
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun GuideTourPublishStep1LocationDateScreen(
    uiState: GuideTourPublishUiState,
    onLocationSelected: (LocationOption) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onStartTimeSelected: (LocalTime) -> Unit,
    onDurationSelected: (Int) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showLocationPicker by rememberSaveable { mutableStateOf(false) }

    GuideTourPublishStep1LocationDateContent(
        uiState = uiState,
        onLocationClick = { showLocationPicker = true },
        onDateSelected = onDateSelected,
        onStartTimeSelected = onStartTimeSelected,
        onDurationSelected = onDurationSelected,
        onNext = onNext,
        modifier = modifier,
    )

    LocationSelectionBottomSheet(
        isVisible = showLocationPicker,
        selectedCountryCode = uiState.countryCode,
        onDismissRequest = { showLocationPicker = false },
        onLocationSelected = { location ->
            onLocationSelected(location)
            showLocationPicker = false
        },
    )
}
