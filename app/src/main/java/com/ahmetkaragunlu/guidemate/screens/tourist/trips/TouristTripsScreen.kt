package com.ahmetkaragunlu.guidemate.screens.tourist.trips

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.tourist.shared.GuideMateTabRow
import compose.icons.TablerIcons
import compose.icons.tablericons.Calendar
import compose.icons.tablericons.MapPin

@Composable
fun TouristTripsScreen(
    viewModel: TouristTripsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val trips by viewModel.trips.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {

        GuideMateTabRow(
            tabs = TripTab.entries,
            selectedTab = selectedTab,
            onTabSelected = { newTab ->
                viewModel.changeTab(newTab)
            }
        )

        LazyColumn(
            contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            items(trips) { trip ->
                TripCard(trip = trip)
            }
        }
    }
}

@Composable
fun TripCard(trip: TripUiModel) {
    val alphaValue = if (trip.isPast) 0.9f else 1f
    val colorFilter = if (trip.isPast) {
        ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
    } else null

    Card(
        modifier = Modifier
            .widthIn(max = 720.dp)
            .fillMaxWidth()
            .height(128.dp)
            .alpha(alphaValue),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {

            Image(
                painter = painterResource(id = trip.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = colorFilter,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.42f)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight().weight(0.58f)
                    .padding(dimensionResource(R.dimen.spacing_medium)),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = trip.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 2.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = colorResource(R.color.text_color),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = trip.date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.text_color),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    Icon(
                        imageVector = TablerIcons.MapPin,
                        contentDescription = null,
                        tint = colorResource(R.color.text_color),

                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = trip.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.text_color),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}