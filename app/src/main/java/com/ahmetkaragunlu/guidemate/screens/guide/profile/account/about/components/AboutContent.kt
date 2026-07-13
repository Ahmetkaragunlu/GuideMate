package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditTextField
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.model.GuideAboutUiState
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideSpokenLanguageUi

@Composable
fun AboutContent(
    uiState: GuideAboutUiState,
    onSpecialtyTitleChange: (String) -> Unit,
    onBiographyChange: (String) -> Unit,
    onRemoveLanguageClick: (String) -> Unit,
    onAddLanguageClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .widthIn(max = 380.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            SpecialtySection(
                title = uiState.specialtyTitle,
                onTitleChange = onSpecialtyTitleChange,
                isError = uiState.showValidationErrors && !uiState.isSpecialtyTitleValid,
            )

            BiographySection(
                biography = uiState.biography,
                onBiographyChange = onBiographyChange,
                isError = uiState.showValidationErrors && !uiState.isBiographyValid,
            )

            LanguagesSection(
                languages = uiState.spokenLanguages,
                onRemoveLanguageClick = onRemoveLanguageClick,
                onAddLanguageClick = onAddLanguageClick,
            )
            if (uiState.showValidationErrors) {
                Text(
                    text = stringResource(R.string.about_form_validation_error),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            EditButton(
                text = R.string.about_save_action,
                onClick = onSaveClick,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_extra_large)),
            )
        }
    }
}

@Composable
private fun SpecialtySection(
    title: String,
    onTitleChange: (String) -> Unit,
    isError: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
    ) {
        Text(
            text = stringResource(R.string.about_specialty_title),
            style = MaterialTheme.typography.titleSmall,
            color = colorResource(R.color.brand_color),
        )

        EditTextField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = R.string.about_specialty_placeholder,
            supportingText = if (isError) R.string.about_specialty_validation_error else null,
            keyboardOptions = KeyboardOptions.Default,
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.brand_color),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    cursorColor = colorResource(R.color.brand_color),
                ),
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = stringResource(R.string.about_specialty_helper),
            style = MaterialTheme.typography.bodySmall,
            color = colorResource(R.color.text_color),
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_tiny)),
        )
    }
}

@Composable
private fun BiographySection(
    biography: String,
    onBiographyChange: (String) -> Unit,
    isError: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
    ) {
        Text(
            text = stringResource(R.string.about_biography_title),
            style = MaterialTheme.typography.titleSmall,
            color = colorResource(R.color.brand_color),
        )

        OutlinedTextField(
            value = biography,
            onValueChange = onBiographyChange,
            modifier = Modifier.fillMaxWidth().height(160.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.about_biography_placeholder),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )
            },
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.brand_color),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    cursorColor = colorResource(R.color.brand_color),
                ),
            supportingText =
                if (isError) {
                    {
                        Text(
                            text = stringResource(R.string.about_biography_validation_error),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                } else {
                    null
                },
        )
    }
}

@Composable
private fun LanguagesSection(
    languages: List<GuideSpokenLanguageUi>,
    onRemoveLanguageClick: (String) -> Unit,
    onAddLanguageClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.about_languages_title),
            style = MaterialTheme.typography.titleSmall,
            color = colorResource(R.color.brand_color),
        )

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            languages.forEach { language ->
                LanguageChip(
                    language = language,
                    onRemoveClick = { onRemoveLanguageClick(language.code) },
                )
            }

            AddLanguageChip(onClick = onAddLanguageClick)
        }
    }
}

@Composable
private fun LanguageChip(
    language: GuideSpokenLanguageUi,
    onRemoveClick: () -> Unit,
) {
    Surface(
        color = colorResource(R.color.brand_color).copy(alpha = 0.1f),
        shape = RoundedCornerShape(20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = dimensionResource(R.dimen.spacing_small)),
        ) {
            Text(
                text = language.displayText,
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(R.color.brand_color),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = colorResource(R.color.brand_color),
                modifier =
                    Modifier
                        .size(dimensionResource(R.dimen.spacing_medium))
                        .clickable(onClick = onRemoveClick),
            )
        }
    }
}

@Composable
private fun AddLanguageChip(
    onClick: () -> Unit,
) {
    Surface(
        color = Color.Transparent,
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
