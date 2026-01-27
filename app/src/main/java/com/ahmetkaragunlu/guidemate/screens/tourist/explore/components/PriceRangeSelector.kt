package com.ahmetkaragunlu.guidemate.screens.tourist.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import java.text.NumberFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceRangeSelector(
    range: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    minPrice: Float = 0f,
    maxPrice: Float = 1000f
) {
    val currentLocale = Locale.getDefault()

    val currencyFormatter = remember(currentLocale) {
        NumberFormat.getCurrencyInstance(currentLocale).apply {
            maximumFractionDigits = 0
        }
    }

    Column(
        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_small))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currencyFormatter.format(range.start),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.brand_color)
            )

            val endPriceText = if (range.endInclusive >= maxPrice) {
                stringResource(R.string.price_plus, currencyFormatter.format(maxPrice))
            } else {
                currencyFormatter.format(range.endInclusive)
            }

            Text(
                text = endPriceText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.brand_color)
            )
        }

        RangeSlider(
            value = range,
            onValueChange = onRangeChange,
            valueRange = minPrice..maxPrice,
            startThumb = {
                CustomSliderThumb(
                    thumbColor = colorResource(R.color.brand_color)
                )
            },
            endThumb = {
                CustomSliderThumb(
                    thumbColor = colorResource(R.color.brand_color)
                )
            },
            track = { rangeSliderState ->
                SliderDefaults.Track(
                    rangeSliderState = rangeSliderState,
                    modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)),
                    colors = SliderDefaults.colors(
                        activeTrackColor = colorResource(R.color.brand_color),
                        inactiveTrackColor = Color.LightGray.copy(alpha = 0.3f),
                        activeTickColor = Color.Transparent,
                        inactiveTickColor = Color.Transparent
                    ),
                    drawStopIndicator = null
                )
            }
        )
    }
}

@Composable
fun CustomSliderThumb(
    thumbColor: Color,
    thumbSize: Dp = dimensionResource(R.dimen.spacing_medium)
) {
    Box(
        modifier = Modifier
            .size(thumbSize)
            .shadow(
                elevation = 2.dp,
                shape = CircleShape,
                clip = false
            )
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = thumbColor,
                shape = CircleShape
            )
    )
}