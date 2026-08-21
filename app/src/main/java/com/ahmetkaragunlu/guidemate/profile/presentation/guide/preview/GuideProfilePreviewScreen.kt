package com.ahmetkaragunlu.guidemate.profile.presentation.guide.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GuideProfilePreviewScreen(
    modifier: Modifier = Modifier,
    viewModel: GuideProfilePreviewViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    GuideProfilePreviewContent(
        uiState = uiState.value,
        modifier = modifier,
    )
}
