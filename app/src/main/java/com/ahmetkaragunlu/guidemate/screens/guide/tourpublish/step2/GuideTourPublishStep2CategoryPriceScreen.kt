package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ahmetkaragunlu.guidemate.screens.common.selection.components.LanguageSelectionBottomSheet
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.LanguageOption
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategory
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step2.content.GuideTourPublishStep2CategoryPriceContent
import com.ahmetkaragunlu.guidemate.screens.guide.tours.category.GuideTourCategorySelectionBottomSheet

@Composable
fun GuideTourPublishStep2CategoryPriceScreen(
    uiState: GuideTourPublishUiState,
    onCategorySelected: (TourCategory) -> Unit,
    onLanguagesSelected: (List<LanguageOption>) -> Unit,
    onRemoveLanguageClick: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCapacityChange: (String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showCategoryPicker by rememberSaveable { mutableStateOf(false) }
    var showLanguagePicker by rememberSaveable { mutableStateOf(false) }

    GuideTourPublishStep2CategoryPriceContent(
        uiState = uiState,
        onCategoryClick = { showCategoryPicker = true },
        onAddLanguageClick = { showLanguagePicker = true },
        onRemoveLanguageClick = onRemoveLanguageClick,
        onPriceChange = onPriceChange,
        onCapacityChange = onCapacityChange,
        onNext = onNext,
        modifier = modifier,
    )

    GuideTourCategorySelectionBottomSheet(
        isVisible = showCategoryPicker,
        selectedCategory = uiState.category,
        onDismissRequest = { showCategoryPicker = false },
        onCategorySelected = onCategorySelected,
    )

    LanguageSelectionBottomSheet(
        isVisible = showLanguagePicker,
        selectedLanguageCodes = uiState.spokenLanguages.mapTo(mutableSetOf()) { it.code },
        onDismissRequest = { showLanguagePicker = false },
        onLanguagesSelected = { languages ->
            onLanguagesSelected(languages)
            showLanguagePicker = false
        },
    )
}
