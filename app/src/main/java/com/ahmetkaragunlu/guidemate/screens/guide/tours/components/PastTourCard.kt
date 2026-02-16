package com.ahmetkaragunlu.guidemate.screens.guide.tours.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourUiModel
import compose.icons.TablerIcons
import compose.icons.tablericons.MapPin
import compose.icons.tablericons.Users

@Composable
fun PastTourCard(
    tour: GuideTourUiModel,
    onDetails: () -> Unit
) {
    val matrix = ColorMatrix().apply { setToSaturation(0.7f) }

    TourBaseCard(
        imageUrl = tour.imageUrl,
        colorFilter = ColorFilter.colorMatrix(matrix),
        alpha = 0.85f,
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacing_medium)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = tour.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    InfoRow(Icons.Default.CalendarMonth, tour.date)
                    InfoRow(TablerIcons.MapPin, tour.location)
                    InfoRow(
                        icon = TablerIcons.Users,
                        text = stringResource(R.string.participant_count, tour.participantCount)
                    )
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = colorResource(R.color.text_color),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp)
                        .clickable { onDetails() }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.spacing_medium))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.earnings_format, tour.earnings ?: 0.0),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Green
                    )

                    if (tour.rating != null && tour.reviewCount != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
                            Text(
                                text = "${tour.rating} (${tour.reviewCount})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorResource(R.color.text_color)
                            )
                        }
                    }
                }
            }
        }
    }
}