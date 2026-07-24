package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step2.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditDropdown
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategory
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategoryCatalog
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.components.GuideTourPublishStepProgress
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.components.GuideTourPublishValidationMessage
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishStep
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tours.components.GuideTourCapacityField
import com.ahmetkaragunlu.guidemate.screens.guide.tours.components.GuideTourLanguageSelector
import com.ahmetkaragunlu.guidemate.screens.guide.tours.components.GuideTourPriceField

@Composable
fun GuideTourPublishStep2CategoryPriceContent(
    uiState: GuideTourPublishUiState,
    onCategoryClick: () -> Unit,
    onAddLanguageClick: () -> Unit,
    onRemoveLanguageClick: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCapacityChange: (String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
        ) {
            GuideTourPublishStepProgress(
                progressLabelResId = R.string.guide_tour_publish_step2_progress_label,
                filledStepIndexInclusive = 1,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_medium)),
            )

            Step2Header()
            Step2CategoryField(
                category = uiState.category,
                onClick = onCategoryClick,
            )
            GuideTourLanguageSelector(
                languages = uiState.spokenLanguages,
                onRemoveLanguage = onRemoveLanguageClick,
                onAddLanguage = onAddLanguageClick,
            )
            GuideTourPriceField(
                value = uiState.price,
                onValueChange = onPriceChange,
            )
            GuideTourCapacityField(
                value = uiState.capacity,
                onValueChange = onCapacityChange,
            )
            GuideTourPublishValidationMessage(
                errorResId = uiState.validationErrorFor(GuideTourPublishStep.TECHNICAL_DETAILS),
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        EditButton(
            text = R.string.guide_tour_publish_step2_next_button,
            onClick = onNext,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_extra_large)),
        )
    }
}

@Composable
private fun Step2Header() {
    Text(
        text = stringResource(R.string.guide_tour_publish_step2_title),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )

    Text(
        text = stringResource(R.string.guide_tour_publish_step2_subtitle),
        style = MaterialTheme.typography.bodyMedium,
        color = colorResource(R.color.text_color),
    )
}

@Composable
private fun Step2CategoryField(
    category: TourCategory?,
    onClick: () -> Unit,
) {
    Text(
        text = stringResource(R.string.guide_tour_publish_step2_category_label),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )

    EditDropdown(
        value =
            category
                ?.let(TourCategoryCatalog::uiModelFor)
                ?.let { stringResource(it.titleResId) }
                .orEmpty(),
        placeholder = R.string.guide_tour_publish_step2_category_label,
        onClick = onClick,
        leadingIcon = { Text(text = "🏷️") },
        modifier = Modifier.fillMaxWidth(),
    )
}
