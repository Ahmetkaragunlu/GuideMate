package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step2.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditTextField
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.components.GuideTourPublishStepProgress
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourLanguageUi
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState

@Composable
fun GuideTourPublishStep2CategoryPriceContent(
    uiState: GuideTourPublishUiState,
    onCategoryClick: () -> Unit,
    onAddLanguageClick: () -> Unit,
    onRemoveLanguageClick: (String) -> Unit,
    onPriceChange: (String) -> Unit,
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
            Step2LanguagesRow(
                languages = uiState.spokenLanguages,
                onRemoveLanguageClick = onRemoveLanguageClick,
                onAddLanguageClick = onAddLanguageClick,
            )
            Step2PriceField(
                price = uiState.price,
                onPriceChange = onPriceChange,
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
    category: String,
    onClick: () -> Unit,
) {
    Text(
        text = stringResource(R.string.guide_tour_publish_step2_category_label),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )

    EditTextField(
        value = category,
        onValueChange = { },
        readOnly = true,
        keyboardOptions = KeyboardOptions.Default,
        leadingIcon = { Text(text = "🏷️") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = colorResource(R.color.text_color),
                modifier = Modifier.clickable(onClick = onClick),
            )
        },
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFEEEDF1),
                unfocusedBorderColor = Color(0xFFEEEDF1),
                cursorColor = Color.Transparent,
                unfocusedTextColor = colorResource(R.color.text_color),
            ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun Step2LanguagesRow(
    languages: List<GuideTourLanguageUi>,
    onRemoveLanguageClick: (String) -> Unit,
    onAddLanguageClick: () -> Unit,
) {
    Text(
        text = stringResource(R.string.guide_tour_publish_step2_languages_label),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
        color = colorResource(R.color.brand_color),
    )

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        languages.forEach { language ->
            LanguageChip(
                language = language,
                onRemoveLanguageClick = { onRemoveLanguageClick(language.code) },
            )
        }
        AddLanguageChip(onClick = onAddLanguageClick)
    }
}

@Composable
private fun LanguageChip(
    language: GuideTourLanguageUi,
    onRemoveLanguageClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .background(
                    color = colorResource(R.color.brand_color).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp),
                )
                .padding(
                    horizontal = 14.dp,
                    vertical = dimensionResource(R.dimen.spacing_small),
                ),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = language.chipLabel,
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(R.color.brand_color),
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = colorResource(R.color.brand_color),
                modifier =
                    Modifier
                        .size(dimensionResource(R.dimen.spacing_medium))
                        .clickable(onClick = onRemoveLanguageClick),
            )
        }
    }
}

@Composable
private fun AddLanguageChip(
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .drawBehind {
                    drawRoundRect(
                        color = Color(0xFFE0E0E0),
                        style =
                            Stroke(
                                width = 3f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f),
                            ),
                        cornerRadius = CornerRadius(20.dp.toPx()),
                    )
                }
                .clickable(onClick = onClick)
                .padding(
                    horizontal = dimensionResource(R.dimen.spacing_medium),
                    vertical = dimensionResource(R.dimen.spacing_small),
                ),
    ) {
        Text(
            text = stringResource(R.string.about_add_language),
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(R.color.text_color),
        )
    }
}

@Composable
private fun Step2PriceField(
    price: String,
    onPriceChange: (String) -> Unit,
) {
    Text(
        text = stringResource(R.string.guide_tour_publish_step2_price_label),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
        color = colorResource(R.color.brand_color),
    )

    EditTextField(
        value = price,
        onValueChange = onPriceChange,
        placeholderText = "1500",
        placeholderColor = colorResource(R.color.brand_color),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        leadingIcon = {
            Text(
                text = "₺",
                color = colorResource(R.color.brand_color),
            )
        },
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFEEEDF1),
                unfocusedBorderColor = Color(0xFFEEEDF1),
                cursorColor = Color.Transparent,
                focusedPlaceholderColor = colorResource(R.color.brand_color),
                unfocusedPlaceholderColor = colorResource(R.color.brand_color),
                focusedTextColor = colorResource(R.color.brand_color),
                unfocusedTextColor = colorResource(R.color.brand_color),
            ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        modifier = Modifier.fillMaxWidth(),
    )
}
