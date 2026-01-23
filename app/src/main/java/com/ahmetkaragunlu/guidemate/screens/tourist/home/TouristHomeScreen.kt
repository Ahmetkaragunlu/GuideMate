package com.ahmetkaragunlu.guidemate.screens.tourist.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R

@Composable
fun TouristHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: TouristHomeViewModel = hiltViewModel()
) {
    val categories = viewModel.categories
    val selectedIndex by viewModel.selectedCategoryIndex.collectAsStateWithLifecycle()
    val popularTours by viewModel.popularTours.collectAsStateWithLifecycle()
    val bestGuides by viewModel.bestGuides.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.spacing_medium)),
        contentPadding = PaddingValues(bottom = dimensionResource(R.dimen.spacing_double_extra_large))
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
            ) {
                Text(
                    text = stringResource(id = R.string.categories),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small))
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                ) {
                    itemsIndexed(categories) { index, category ->
                        CategoryCard(
                            category = category,
                            isSelected = (index == selectedIndex),
                            onClick = { viewModel.updateSelectedCategory(index) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

                Text(
                    text = stringResource(id = R.string.popular_experiences),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small))
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(popularTours) { tour ->
                        PopularTourCard(tour = tour)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        }

        item {
            Text(
                text = stringResource(id = R.string.best_guides_in_region),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.spacing_small)
                )
            )
        }

        item {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
        }

        items(bestGuides) { guide ->
            BestGuideCard(guide = guide)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
        }
    }
}





@Composable
fun CategoryCard(
    category: CategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF4FC3F7) else MaterialTheme.colorScheme.onPrimary,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else colorResource(R.color.text_color)
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(category.icon, null)
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
            Text(
                text = stringResource(id = category.titleResId),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}