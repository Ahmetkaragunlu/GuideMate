package com.ahmetkaragunlu.guidemate.profile.presentation.publicprofile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.profile.presentation.components.GuideProfileContent

@Composable
fun GuidePublicProfileScreen(
    guideId: Long,
    modifier: Modifier = Modifier,
    viewModel: GuidePublicProfileViewModel = hiltViewModel(),
    onMessageClick: () -> Unit = {},
    onTourClick: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(guideId) {
        viewModel.loadGuide(guideId)
    }

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = viewModel::retry,
        modifier = modifier,
    ) {
        GuideProfileContent(
            uiState = uiState,
            modifier = modifier,
            onMessageClick = onMessageClick,
            onTourClick = onTourClick,
        )
    }
}
