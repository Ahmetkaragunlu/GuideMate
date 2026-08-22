package com.ahmetkaragunlu.guidemate.profile.presentation.guide.account.about

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.common.location.presentation.components.LanguageSelectionBottomSheet
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.account.about.components.AboutContent

@Composable
fun AboutScreen(
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuideAboutViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLanguagePicker by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.onErrorShown()
        }
    }
    LaunchedEffect(uiState.saveCompleted) {
        if (uiState.saveCompleted) {
            viewModel.onSaveHandled()
            onSaved()
        }
    }

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = viewModel::refreshProfile,
        modifier = modifier.fillMaxSize(),
    ) {
        AboutContent(
            modifier = modifier,
            uiState = uiState,
            onSpecialtyTitleChange = viewModel::onSpecialtyTitleChange,
            onBiographyChange = viewModel::onBiographyChange,
            onRemoveLanguageClick = viewModel::onRemoveLanguageClick,
            onAddLanguageClick = { showLanguagePicker = true },
            onSaveClick = viewModel::onSaveClick,
        )
    }

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
