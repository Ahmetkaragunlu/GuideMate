package com.ahmetkaragunlu.guidemate.screens.tourist.explore

import androidx.compose.runtime.Composable


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditTextField
import com.ahmetkaragunlu.guidemate.screens.tourist.explore.model.ExploreTab
import compose.icons.TablerIcons
import compose.icons.tablericons.AdjustmentsHorizontal

@Composable
fun TouristExploreScreen(
    modifier: Modifier = Modifier,
    viewModel: TouristExploreViewModel = hiltViewModel(),
    onNavigateToFilter: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabs = ExploreTab.entries

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = uiState.selectedTab.ordinal,
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTab.ordinal]),
                    height = 2.dp,
                    color = colorResource(R.color.brand_color)
                )
            },

            ) {
            tabs.forEach { tab ->
                Tab(
                    selected = uiState.selectedTab == tab,
                    onClick = { viewModel.updateSelectedTab(tab) },
                    text = {
                        Text(
                            text = stringResource(id = tab.titleResId),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (uiState.selectedTab == tab) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    },
                    selectedContentColor = colorResource(R.color.brand_color),
                    unselectedContentColor = colorResource(R.color.text_color)
                )
            }
        }

        when (uiState.selectedTab) {
            ExploreTab.TOURS -> ToursContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.spacing_medium)),
                onNavigateToFilter = onNavigateToFilter
            )

            ExploreTab.GUIDES -> GuidesContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.spacing_medium))
            )
        }
    }
}

@Composable
private fun ToursContent(
    modifier: Modifier = Modifier,
    onNavigateToFilter: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.spacing_medium))
    ) {
        EditTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.brand_color),
                unfocusedBorderColor = colorResource(R.color.brand_color),
                cursorColor = colorResource(R.color.brand_color),


                ),
            placeholder = R.string.search_tours,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            trailingIcon = {
                IconButton(onClick = { onNavigateToFilter() }) {
                    Icon(
                        imageVector = TablerIcons.AdjustmentsHorizontal,
                        contentDescription = null,
                        tint = colorResource(R.color.brand_color)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )

    }
}

@Composable
private fun GuidesContent(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.spacing_medium))
    ) {
        EditTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.brand_color),
                unfocusedBorderColor = colorResource(R.color.brand_color),
                cursorColor = colorResource(R.color.brand_color),
                ),
            placeholder = R.string.search_guide,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}