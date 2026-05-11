package com.ahmetkaragunlu.guidemate.screens.guide.profile.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.screens.guide.profile.preview.components.GuideProfilePreviewContent
import com.ahmetkaragunlu.guidemate.screens.guide.profile.preview.viewmodel.GuideProfilePreviewViewModel

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
