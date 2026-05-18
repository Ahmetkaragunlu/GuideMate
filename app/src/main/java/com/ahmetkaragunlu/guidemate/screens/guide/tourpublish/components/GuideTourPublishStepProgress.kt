package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R

@Composable
fun GuideTourPublishStepProgress(
    @StringRes progressLabelResId: Int,
    filledStepIndexInclusive: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(progressLabelResId),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.brand_color),
        textAlign = TextAlign.End,
        modifier =
            modifier
                .fillMaxWidth(),
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
    ) {
        repeat(4) { index ->
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .height(6.dp)
                        .background(
                            color =
                                if (index <= filledStepIndexInclusive) {
                                    colorResource(R.color.brand_color)
                                } else {
                                    Color(0xFFE5E7EB)
                                },
                            shape = RoundedCornerShape(
                                dimensionResource(R.dimen.radius_small),
                            ),
                        ),
            )
        }
    }
    Spacer(modifier = Modifier.height(2.dp))
}
