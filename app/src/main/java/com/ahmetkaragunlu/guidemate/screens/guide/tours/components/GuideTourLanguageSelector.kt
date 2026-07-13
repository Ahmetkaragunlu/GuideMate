package com.ahmetkaragunlu.guidemate.screens.guide.tours.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourLanguage

@Composable
fun GuideTourLanguageSelector(
    languages: List<TourLanguage>,
    onRemoveLanguage: (String) -> Unit,
    onAddLanguage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.spoken_languages),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
        color = colorResource(R.color.brand_color),
    )
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        languages.forEach { language ->
            Row(
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
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${language.flagEmoji} ${language.displayName}",
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
                            .clickable { onRemoveLanguage(language.code) },
                )
            }
        }
        Box(
            modifier =
                Modifier
                    .drawBehind {
                        drawRoundRect(
                            color = Color(0xFFE0E0E0),
                            style =
                                Stroke(
                                    width = 3f,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f)),
                                ),
                            cornerRadius = CornerRadius(20.dp.toPx()),
                        )
                    }
                    .clickable(onClick = onAddLanguage)
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
}
