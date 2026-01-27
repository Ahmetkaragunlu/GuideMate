package com.ahmetkaragunlu.guidemate.screens.tourist.explore.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R

@Composable
fun RatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxStars: Int = 5
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny))
    ) {
        repeat(maxStars) { index ->
            val starIndex = index + 1
            Icon(
                imageVector = if (starIndex <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = null,
                tint = Color(0xFFFF9F1C),
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onRatingChanged(starIndex) }
            )
        }
    }
}
