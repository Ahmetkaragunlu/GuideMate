package com.ahmetkaragunlu.guidemate.screens.guide.tours.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tours.InfoRow
import com.ahmetkaragunlu.guidemate.screens.common.tours.TourBaseCard
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourCardUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourApprovalStatus
import compose.icons.TablerIcons
import compose.icons.tablericons.Edit
import compose.icons.tablericons.MapPin
import compose.icons.tablericons.Trash

@Composable
fun ReviewTourCard(
    tour: GuideTourCardUiModel,
    onEdit: () -> Unit,
    onArchive: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TourBaseCard(
        imageResId = tour.imageResId,
        imageUrl = tour.imageUrl,
        modifier = modifier.clickable(onClick = onClick),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacing_medium)),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = tour.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            InfoRow(Icons.Default.CalendarMonth, tour.date)
            InfoRow(TablerIcons.MapPin, tour.location)
            Text(
                text =
                    stringResource(
                        if (tour.approvalStatus == TourApprovalStatus.PENDING_REVIEW) {
                            R.string.pending_review
                        } else {
                            R.string.rejected
                        },
                    ),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color =
                    if (tour.approvalStatus == TourApprovalStatus.PENDING_REVIEW) {
                        Color(0xFFFF9800)
                    } else {
                        MaterialTheme.colorScheme.error
                    },
            )
            tour.rejectionReason?.let { reason ->
                Text(
                    text = stringResource(R.string.rejection_reason_format, reason),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.text_color),
                )
            }
            if (tour.approvalStatus == TourApprovalStatus.REJECTED) {
                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (tour.canArchive) {
                        ReviewCardAction(
                            textResId = R.string.archive_tour_draft,
                            icon = TablerIcons.Trash,
                            color = MaterialTheme.colorScheme.error,
                            onClick = onArchive,
                        )
                    }
                    ReviewCardAction(
                        textResId = R.string.edit,
                        icon = TablerIcons.Edit,
                        color = colorResource(R.color.text_color),
                        onClick = onEdit,
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewCardAction(
    textResId: Int,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .clickable(onClick = onClick)
                .padding(dimensionResource(R.dimen.spacing_small)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
        Text(
            text = stringResource(textResId),
            style = MaterialTheme.typography.labelMedium,
            color = color,
        )
    }
}
