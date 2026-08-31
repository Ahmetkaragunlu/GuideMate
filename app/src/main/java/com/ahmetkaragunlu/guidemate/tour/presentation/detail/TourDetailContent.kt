package com.ahmetkaragunlu.guidemate.tour.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditButton
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateTabRow
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailMode
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailTab
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState

@Composable
fun TourDetailContent(
    uiState: TourDetailUiState,
    mode: TourDetailMode,
    onPrimaryAction: () -> Unit,
    modifier: Modifier = Modifier,
    topContent: (@Composable () -> Unit)? = null,
    onGuideProfileClick: (() -> Unit)? = null,
    isPrimaryActionLoading: Boolean = false,
    isPrimaryActionEnabled: Boolean = true,
) {
    var selectedTab by rememberSaveable { mutableStateOf(TourDetailTab.DETAILS) }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
    ) {
        Column(
            modifier =
                Modifier
                    .widthIn(max = 380.dp)
                    .fillMaxWidth(),
        ) {
            TourDetailSummary(
                uiState = uiState,
                mode = mode,
                topContent = topContent,
                onGuideProfileClick = onGuideProfileClick,
            )
            GuideMateTabRow(
                tabs = TourDetailTab.entries,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
            )
            TourDetailTabContent(
                selectedTab = selectedTab,
                uiState = uiState,
            )
        }

        mode.primaryActionTextResId?.let { actionTextResId ->
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
            EditButton(
                text = actionTextResId,
                onClick = onPrimaryAction,
                icon = mode.primaryActionIcon,
                enabled = isPrimaryActionEnabled,
                isLoading = isPrimaryActionLoading,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_extra_large)),
            )
        }
    }
}
