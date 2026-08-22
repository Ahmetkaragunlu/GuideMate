package com.ahmetkaragunlu.guidemate.profile.presentation.guide.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.profile.presentation.components.GuideProfileContent

@Composable
fun GuideProfilePreviewScreen(
    modifier: Modifier = Modifier,
    viewModel: GuideProfilePreviewViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    GuideMateContentState(
        state = uiState.value.loadState,
        onRetry = viewModel::refreshProfile,
        modifier = modifier,
    ) {
        GuideProfileContent(
            uiState = uiState.value,
            modifier = modifier,
        )
    }
}
