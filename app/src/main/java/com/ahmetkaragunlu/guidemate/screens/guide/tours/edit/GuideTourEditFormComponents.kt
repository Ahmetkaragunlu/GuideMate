package com.ahmetkaragunlu.guidemate.screens.guide.tours.edit

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditDropdown
import com.ahmetkaragunlu.guidemate.components.EditTextField
import com.ahmetkaragunlu.guidemate.components.GuideMateImage
import com.ahmetkaragunlu.guidemate.screens.guide.tours.edit.model.GuideTourEditUiState

@Composable
internal fun TourMediaEditor(
    uiState: GuideTourEditUiState,
    onChangePhotos: () -> Unit,
) {
    Text(
        text = stringResource(R.string.guide_tour_publish_step3_photos_label),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(dimensionResource(R.dimen.radius_medium)))
                .clickable(onClick = onChangePhotos),
        contentAlignment = Alignment.BottomCenter,
    ) {
        GuideMateImage(
            fallbackImageResId = uiState.coverImageResId,
            imageUrl = uiState.selectedCoverImageUri ?: uiState.coverImageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Text(
            text = stringResource(R.string.change_cover_photo),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacing_small)),
        )
    }
}

@Composable
internal fun TourEditField(
    @StringRes labelResId: Int,
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes placeholderResId: Int? = null,
    leadingText: String? = null,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Text(
        text = stringResource(labelResId),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )
    EditTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        placeholder = placeholderResId,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon = leadingText?.let { { Text(text = it) } },
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFEEEDF1),
                unfocusedBorderColor = Color(0xFFEEEDF1),
                unfocusedTextColor = colorResource(R.color.text_color),
            ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
internal fun TourEditDropdownField(
    @StringRes labelResId: Int,
    value: String,
    @StringRes placeholderResId: Int,
    leadingText: String,
    onClick: () -> Unit = {},
) {
    Text(
        text = stringResource(labelResId),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )
    EditDropdown(
        value = value,
        placeholder = placeholderResId,
        onClick = onClick,
        leadingIcon = { Text(text = leadingText) },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
internal fun TourEditNumericField(
    @StringRes labelResId: Int,
    value: String,
    onValueChange: (String) -> Unit,
    leadingText: String,
    useBrandTextColor: Boolean,
) {
    Text(
        text = stringResource(labelResId),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
        color = colorResource(R.color.brand_color),
    )
    EditTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions =
            KeyboardOptions(
                keyboardType =
                    if (useBrandTextColor) {
                        KeyboardType.Decimal
                    } else {
                        KeyboardType.Number
                    },
            ),
        leadingIcon = {
            if (useBrandTextColor) {
                Text(
                    text = leadingText,
                    color = colorResource(R.color.brand_color),
                )
            } else {
                Text(text = leadingText)
            }
        },
        colors =
            if (useBrandTextColor) {
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFEEEDF1),
                    unfocusedBorderColor = Color(0xFFEEEDF1),
                    cursorColor = Color.Transparent,
                    focusedTextColor = colorResource(R.color.brand_color),
                    unfocusedTextColor = colorResource(R.color.brand_color),
                )
            } else {
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFEEEDF1),
                    unfocusedBorderColor = Color(0xFFEEEDF1),
                    cursorColor = Color.Transparent,
                )
            },
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
internal fun TourEditMultilineField(
    @StringRes labelResId: Int,
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes placeholderResId: Int,
    enabled: Boolean = true,
    height: Dp,
) {
    Text(
        text = stringResource(labelResId),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        placeholder = {
            Text(
                text = stringResource(placeholderResId),
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray,
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFEEEDF1),
                unfocusedBorderColor = Color(0xFFEEEDF1),
            ),
        modifier =
            Modifier
                .fillMaxWidth()
                .height(height),
    )
}
