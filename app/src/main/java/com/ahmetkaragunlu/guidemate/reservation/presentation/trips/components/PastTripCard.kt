package com.ahmetkaragunlu.guidemate.reservation.presentation.trips.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toPlatformCurrencyFromMinorUnit
import com.ahmetkaragunlu.guidemate.tour.presentation.components.InfoRow
import com.ahmetkaragunlu.guidemate.tour.presentation.components.TourBaseCard
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model.TripUiModel
import compose.icons.TablerIcons
import compose.icons.tablericons.MapPin
import compose.icons.tablericons.Users

@Composable
fun PastTripCard(
    trip: TripUiModel,
    onDetailsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val matrix = ColorMatrix().apply { setToSaturation(0.7f) }

    TourBaseCard(
        imageResId = trip.imageResId,
        imageUrl = trip.imageUrl,
        modifier = modifier.clickable(onClick = onDetailsClick),
        colorFilter = ColorFilter.colorMatrix(matrix),
        alpha = 0.85f,
        elevation = 2.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.spacing_medium)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = trip.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    trip.cancellationTitleResId?.let { titleResId ->
                        Text(
                            text = stringResource(titleResId),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    InfoRow(icon = Icons.Default.CalendarMonth, text = trip.date)
                    InfoRow(icon = TablerIcons.MapPin, text = trip.location)
                    if (trip.cancellationTitleResId == null) {
                        InfoRow(
                            icon = TablerIcons.Users,
                            text =
                                stringResource(
                                    R.string.reservation_participant_count,
                                    trip.participantCount,
                                ),
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = colorResource(R.color.text_color),
                    modifier =
                        Modifier
                            .padding(start = dimensionResource(R.dimen.spacing_medium))
                            .size(24.dp),
                )
            }

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                        .padding(bottom = dimensionResource(R.dimen.spacing_medium)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text =
                        stringResource(
                            R.string.amount_format,
                            trip.totalPriceMinor.toPlatformCurrencyFromMinorUnit(),
                        ),
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(R.color.brand_color),
                )
            }
        }
    }
}
