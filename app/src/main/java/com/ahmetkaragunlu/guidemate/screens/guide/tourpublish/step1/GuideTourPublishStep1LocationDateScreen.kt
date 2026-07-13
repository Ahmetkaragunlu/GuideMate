package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step1

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step1.content.GuideTourPublishStep1LocationDateContent
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun GuideTourPublishStep1LocationDateScreen(
    uiState: GuideTourPublishUiState,
    onLocationClick: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onStartTimeSelected: (LocalTime) -> Unit,
    onDurationSelected: (Int) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GuideTourPublishStep1LocationDateContent(
        uiState = uiState,
        onLocationClick = onLocationClick,
        onDateSelected = onDateSelected,
        onStartTimeSelected = onStartTimeSelected,
        onDurationSelected = onDurationSelected,
        onNext = onNext,
        modifier = modifier,
    )
}
