package com.ahmetkaragunlu.guidemate.screens.guide.tours.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourUiModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Edit
import compose.icons.tablericons.MapPin
import compose.icons.tablericons.Users

@Composable
fun ActiveTourCard(
    tour: GuideTourUiModel,
    onToggleLive: (Boolean) -> Unit,
    onEdit: () -> Unit,
) {
    TourBaseCard(imageUrl = tour.imageUrl) {
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
                        text = tour.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    InfoRow(Icons.Default.CalendarMonth, tour.date)
                    InfoRow(TablerIcons.MapPin, tour.location)
                    Text(
                        text = stringResource(R.string.category_label, tour.category),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.text_color),
                    )
                    if (tour.languagesFlag.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = tour.languagesFlag)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tour.languagesText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorResource(R.color.text_color),
                            )
                        }
                    }
                    InfoRow(
                        icon = TablerIcons.Users,
                        text = stringResource(R.string.participant_count, tour.participantCount),
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                    modifier = Modifier.padding(start = 16.dp),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Switch(
                            checked = tour.isLive,
                            onCheckedChange = onToggleLive,
                            colors =
                                SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                    checkedTrackColor = Color.Green,
                                    uncheckedTrackColor = Color.LightGray,
                                ),
                            modifier = Modifier.scale(0.8f),
                        )
                        Text(
                            text =
                                if (tour.isLive) {
                                    stringResource(R.string.live)
                                } else {
                                    stringResource(
                                        R.string.hidden,
                                    )
                                },
                            style = MaterialTheme.typography.labelSmall,
                            color = if (tour.isLive) Color.Green else Color.LightGray,
                        )
                    }

                    Row(
                        modifier =
                            Modifier
                                .clickable { onEdit() }
                                .padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = TablerIcons.Edit,
                            contentDescription = null,
                            tint = colorResource(R.color.text_color),
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
                        Text(
                            text = stringResource(R.string.edit),
                            style = MaterialTheme.typography.labelMedium,
                            color = colorResource(R.color.text_color),
                        )
                    }
                }
            }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimensionResource(R.dimen.spacing_medium)),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.price_format, tour.price),
                        style = MaterialTheme.typography.titleMedium,
                        color = colorResource(R.color.brand_color),
                    )

                    if (tour.rating != null && tour.reviewCount != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
                            Text(
                                text = "${tour.rating} (${tour.reviewCount})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorResource(R.color.text_color),
                            )
                        }
                    }
                }
            }
        }
    }
}
