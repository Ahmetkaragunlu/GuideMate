package com.ahmetkaragunlu.guidemate.tour.presentation.detail

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailMode
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState

@Composable
internal fun TourDetailSummary(
    uiState: TourDetailUiState,
    mode: TourDetailMode,
    topContent: (@Composable () -> Unit)?,
    onGuideProfileClick: (() -> Unit)?,
) {
    topContent?.invoke()
    if (mode.showPreviewBanner) PreviewBanner()
    HeroSection(uiState = uiState)
    uiState.session.status?.let { status ->
        TourStatusSection(
            status = status,
            cancellationReason = uiState.session.cancellationReason,
        )
    }
    TourDetailSectionDivider()
    DateLocationRow(uiState = uiState)
    TourDetailSectionDivider()
    LanguageCategoryRow(uiState = uiState)
    TourDetailSectionDivider()
    PriceRow(uiState = uiState)
    if (mode.showGuideInfo) {
        TourDetailSectionDivider()
        GuideInfoRow(
            uiState = uiState,
            onGuideProfileClick = onGuideProfileClick,
        )
    }
}

@Composable
internal fun TourDetailSectionDivider() {
    HorizontalDivider(color = colorResource(R.color.divider_color), thickness = 1.dp)
}
