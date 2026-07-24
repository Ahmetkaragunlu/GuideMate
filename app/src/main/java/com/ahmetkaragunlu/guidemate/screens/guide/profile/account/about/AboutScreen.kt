package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.screens.common.selection.components.LanguageSelectionBottomSheet
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.components.AboutContent
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.viewmodel.GuideAboutViewModel

@Composable
fun AboutScreen(
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuideAboutViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLanguagePicker by rememberSaveable { mutableStateOf(false) }

    AboutContent(
        modifier = modifier,
        uiState = uiState,
        onSpecialtyTitleChange = viewModel::onSpecialtyTitleChange,
        onBiographyChange = viewModel::onBiographyChange,
        onRemoveLanguageClick = viewModel::onRemoveLanguageClick,
        onAddLanguageClick = { showLanguagePicker = true },
        onSaveClick = {
            if (viewModel.onSaveClick()) onSaved()
        },
    )

    LanguageSelectionBottomSheet(
        isVisible = showLanguagePicker,
        selectedLanguageCodes = uiState.spokenLanguages.mapTo(mutableSetOf()) { it.code },
        onDismissRequest = { showLanguagePicker = false },
        onLanguagesSelected = { languages ->
            viewModel.onLanguagesSelected(languages)
            showLanguagePicker = false
        },
    )
}
