package com.ahmetkaragunlu.guidemate.screens.guide.tours.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditTextField

@Composable
fun GuideTourPriceField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    Text(
        text = stringResource(R.string.guide_tour_publish_step2_price_label),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
        color = colorResource(R.color.brand_color),
    )

    EditTextField(
        value = value,
        onValueChange = onValueChange,
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

@Composable
fun GuideTourCapacityField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    Text(
        text = stringResource(R.string.guide_tour_publish_step2_capacity_label),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
        color = colorResource(R.color.brand_color),
    )

    EditTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        leadingIcon = { Text(text = "👥") },
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFEEEDF1),
                unfocusedBorderColor = Color(0xFFEEEDF1),
                cursorColor = Color.Transparent,
            ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun GuideTourMeetingPointField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    Text(
        text = stringResource(R.string.guide_tour_publish_step3_meeting_label),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = stringResource(R.string.guide_tour_publish_step3_meeting_placeholder),
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
                .height(130.dp),
    )
}
