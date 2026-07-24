package com.ahmetkaragunlu.guidemate.screens.guide.tours.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import com.ahmetkaragunlu.guidemate.screens.common.tours.InfoRow
import com.ahmetkaragunlu.guidemate.screens.common.tours.TourBaseCard
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategoryCatalog
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourCardUiModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Edit
import compose.icons.tablericons.MapPin
import compose.icons.tablericons.Users

@Composable
fun ActiveTourCard(
    tour: GuideTourCardUiModel,
    onToggleLive: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TourBaseCard(
        imageResId = tour.imageResId,
        imageUrl = tour.imageUrl,
        modifier = modifier.clickable(onClick = onClick),
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
                                R.string.category_label,
                                stringResource(TourCategoryCatalog.uiModelFor(tour.category).titleResId),
                            ),
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
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_medium)),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Switch(
                            checked = tour.isBookingOpen,
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
                                if (tour.isBookingOpen) {
                                    stringResource(R.string.live)
                                } else {
                                    stringResource(
                                        R.string.hidden,
                                    )
                                },
                            style = MaterialTheme.typography.labelSmall,
                            color = if (tour.isBookingOpen) Color.Green else Color.LightGray,
                        )
                    }

                    Row(
                        modifier =
                            Modifier
                                .clickable { onEdit() }
                                .padding(end = dimensionResource(R.dimen.spacing_small)),
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
                                text = stringResource(R.string.rating_review_format, tour.rating, tour.reviewCount),
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
