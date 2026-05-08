package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.components.AboutContent
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.viewmodel.GuideAboutViewModel

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    viewModel: GuideAboutViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AboutContent(
        modifier = modifier,
        uiState = uiState,
        onSpecialtyTitleChange = viewModel::onSpecialtyTitleChange,
        onBiographyChange = viewModel::onBiographyChange,
        onRemoveLanguageClick = viewModel::onRemoveLanguageClick,
        onAddLanguageClick = viewModel::onAddLanguageClick,
        onSaveClick = viewModel::onSaveClick,
    )
}
