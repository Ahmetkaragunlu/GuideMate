package com.ahmetkaragunlu.guidemate.profile.presentation.publicprofile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.profile.presentation.components.GuideProfileContent
import kotlinx.coroutines.launch

@Composable
fun GuidePublicProfileScreen(
    guideId: Long,
    modifier: Modifier = Modifier,
    viewModel: GuidePublicProfileViewModel = hiltViewModel(),
    onNavigateToChat: (String) -> Unit = {},
    onTourClick: (String) -> Unit = {},
    onSeeAllToursClick: (() -> Unit)? = null,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(guideId) {
        viewModel.loadGuide(guideId)
    }

    LaunchedEffect(viewModel) {
        launch { viewModel.chatDestinations.collect(onNavigateToChat) }
        launch { viewModel.chatErrors.collect(snackbarHostState::showSnackbar) }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GuideMateContentState(
            state = uiState.loadState,
            onRetry = viewModel::retry,
            modifier = modifier,
        ) {
            GuideProfileContent(
                uiState = uiState,
                modifier = modifier,
                onMessageClick = { viewModel.startChat(guideId) },
                onTourClick = onTourClick,
                onSeeAllToursClick = onSeeAllToursClick,
            )
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
