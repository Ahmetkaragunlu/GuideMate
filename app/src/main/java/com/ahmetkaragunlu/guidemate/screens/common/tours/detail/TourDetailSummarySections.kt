package com.ahmetkaragunlu.guidemate.screens.common.tours.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.GuideMateImage
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailMode
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailStatus
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailUiState
import compose.icons.TablerIcons
import compose.icons.tablericons.Users

@Composable
internal fun TourDetailSummary(
    uiState: TourDetailUiState,
    mode: TourDetailMode,
    topContent: (@Composable () -> Unit)?,
) {
    topContent?.invoke()
    if (mode.showPreviewBanner) PreviewBanner()
    HeroSection(uiState = uiState)
    uiState.sessionStatus?.let { status ->
        TourStatusSection(
            status = status,
            cancellationReason = uiState.cancellationReason,
        )
    }
    TourDetailSectionDivider()
    DateLocationRow(uiState = uiState)
    TourDetailSectionDivider()
    LanguageCategoryRow(uiState = uiState)
    TourDetailSectionDivider()
    PriceRow(uiState = uiState)
    if (mode.showGuideInfo) {
        TourDetailSectionDivider()
        GuideInfoRow(uiState = uiState)
    }
}

@Composable
private fun TourStatusSection(
    status: TourDetailStatus,
    cancellationReason: String?,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
    ) {
        Text(
            text = stringResource(status.labelResId),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color =
                if (status == TourDetailStatus.CANCELLED) {
                    MaterialTheme.colorScheme.error
                } else {
                    colorResource(R.color.brand_color)
                },
        )
        if (status == TourDetailStatus.CANCELLED && !cancellationReason.isNullOrBlank()) {
            Text(
                text = stringResource(R.string.cancellation_reason_format, cancellationReason),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_color),
            )
        }
    }
}

@Composable
internal fun TourDetailSectionDivider() {
    HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 1.dp)
}

@Composable
private fun PreviewBanner() {
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
private fun HeroSection(uiState: TourDetailUiState) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(top = dimensionResource(R.dimen.spacing_medium)),
    ) {
        GuideMateImage(
            fallbackImageResId = uiState.imageResId,
            imageUrl = uiState.imageUrl,
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
                text = uiState.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
            )
            if (uiState.rating != null && uiState.reviewCount > 0) {
                Text(
                    text = "⭐ ${stringResource(R.string.rating_review_format, uiState.rating, uiState.reviewCount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Composable
private fun DateLocationRow(uiState: TourDetailUiState) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
    ) {
        EmojiLabelItem(
            emoji = "📅",
            label = uiState.date,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (uiState.durationMinutes > 0) {
                EmojiLabelItem(
                    emoji = "⏱️",
                    label = stringResource(R.string.tour_duration_format, uiState.durationMinutes),
                )
            }
            EmojiLabelItem(
                emoji = "🌍",
                label = uiState.location,
            )
        }
    }
}

@Composable
private fun LanguageCategoryRow(uiState: TourDetailUiState) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_medium)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        EmojiLabelItem(
            emoji = uiState.languagesFlag,
            label = uiState.languagesText,
        )
        EmojiLabelItem(
            emoji = "🏷️",
            label = uiState.category,
        )
    }
}

@Composable
private fun EmojiLabelItem(
    emoji: String,
    label: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = emoji)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun PriceRow(uiState: TourDetailUiState) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_medium)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.price_format, uiState.price),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.brand_color),
        )
        if (uiState.capacity > 0) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = TablerIcons.Users,
                    contentDescription = null,
                    tint = colorResource(R.color.text_color),
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text =
                        stringResource(
                            R.string.tour_participant_capacity_format,
                            uiState.bookedCount,
                            uiState.capacity,
                        ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.text_color),
                )
            }
        }
    }
}

@Composable
private fun GuideInfoRow(uiState: TourDetailUiState) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_medium)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = uiState.guideImageResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(42.dp)
                        .clip(CircleShape),
            )
            Column {
                Text(
                    text = uiState.guideName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.guide_tour_publish_step4_guide_role),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.text_color),
                )
            }
        }
        Text(
            text = stringResource(R.string.guide_tour_publish_step4_view_profile),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.brand_color),
            fontWeight = FontWeight.SemiBold,
        )
    }
}
