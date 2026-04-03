package com.ahmetkaragunlu.guidemate.screens.guide.tours

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.tours.components.ActiveTourCard
import com.ahmetkaragunlu.guidemate.screens.guide.tours.components.PastTourCard
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourTab
import com.ahmetkaragunlu.guidemate.screens.common.tab.GuideMateTabRow
import compose.icons.TablerIcons
import compose.icons.tablericons.Plus

@Composable
fun GuideMyToursScreen(
    viewModel: GuideMyToursViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val tours by viewModel.tours.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            GuideMateTabRow(
                tabs = GuideTourTab.entries,
                selectedTab = selectedTab,
                onTabSelected = viewModel::changeTab,
            )

            LazyColumn(
                contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
            ) {
                items(tours, key = { it.id }) { tour ->
                    when (selectedTab) {
                        GuideTourTab.ACTIVE ->
                            ActiveTourCard(
                                tour = tour,
                                onToggleLive = { isLive ->
                                    viewModel.toggleLive(tour.id, isLive)
                                },
                                onEdit = { /* Navigate to edit */ },
                            )
                        GuideTourTab.PAST ->
                            PastTourCard(
                                tour = tour,
                                onDetails = { /* Navigate to details */ },
                            )
                    }
                }
            }
        }

        if (selectedTab == GuideTourTab.ACTIVE) {
            FloatingActionButton(
                onClick = { /* Navigate to create tour */ },
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(dimensionResource(R.dimen.spacing_large)),
                containerColor = colorResource(R.color.brand_color),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
            ) {
                Icon(
                    imageVector = TablerIcons.Plus,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
