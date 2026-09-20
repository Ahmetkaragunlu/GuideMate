package com.ahmetkaragunlu.guidemate.tour.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.image.GuideMateImage
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState

@Composable
internal fun PreviewBanner() {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.spacing_medium))
                .background(
                    color = Color(0xFFF4F7FC),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
                )
                .padding(
                    horizontal = dimensionResource(R.dimen.spacing_medium),
                    vertical = dimensionResource(R.dimen.spacing_small),
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.guide_tour_publish_step4_preview_banner),
            style = MaterialTheme.typography.labelLarge,
            color = colorResource(R.color.text_color),
        )
    }
}

@Composable
internal fun HeroSection(uiState: TourDetailUiState) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(top = dimensionResource(R.dimen.spacing_medium)),
    ) {
        GuideMateImage(
            fallbackImageResId = uiState.tour.imageResId,
            imageUrl = uiState.tour.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        brush =
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.72f),
                                    ),
                            ),
                    ),
        )
        Column(
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(dimensionResource(R.dimen.spacing_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
        ) {
            Text(
                text = uiState.tour.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
            )
            if (uiState.tour.rating != null && uiState.tour.reviewCount > 0) {
                Text(
                    text =
                        "⭐ ${
                            stringResource(
                                R.string.rating_review_format,
                                uiState.tour.rating,
                                uiState.tour.reviewCount,
                            )
                        }",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
