package com.ahmetkaragunlu.guidemate.screens.tourist.trips.components

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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tours.InfoRow
import com.ahmetkaragunlu.guidemate.screens.common.tours.TourBaseCard
import com.ahmetkaragunlu.guidemate.screens.tourist.trips.TripUiModel
import compose.icons.TablerIcons
import compose.icons.tablericons.MapPin
import compose.icons.tablericons.Users

@Composable
fun UpcomingTripCard(
    trip: TripUiModel,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TourBaseCard(
        imageResId = trip.imageResId,
        modifier = modifier,
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
                    InfoRow(icon = Icons.Default.CalendarMonth, text = trip.date)
                    InfoRow(icon = TablerIcons.MapPin, text = trip.location)
                    Text(
                        text = stringResource(R.string.category_label, trip.category),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.text_color),
                    )
                    if (trip.languagesFlag.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = trip.languagesFlag)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = trip.languagesText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorResource(R.color.text_color),
                            )
                        }
                    }
                    InfoRow(
                        icon = TablerIcons.Users,
                        text = stringResource(R.string.participant_count, trip.participantCount),
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_medium)),
                ) {
                    if (trip.rating != null && trip.reviewCount != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                            )
                            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
                            Text(
                                text = stringResource(
                                    R.string.rating_review_format,
                                    trip.rating,
                                    trip.reviewCount
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorResource(R.color.text_color),
                            )
                        }
                    }
                }
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
                    text = stringResource(R.string.price_format, trip.price),
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(R.color.brand_color),
                )

                Text(
                    text = stringResource(R.string.cancel_action),
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .clickable(onClick = onCancelClick)
                        .padding(end = dimensionResource(R.dimen.spacing_small)),
                )
            }
        }
    }
}
