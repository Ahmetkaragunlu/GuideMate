package com.ahmetkaragunlu.guidemate.screens.tourist.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.tourist.home.components.BestGuideCard
import com.ahmetkaragunlu.guidemate.screens.tourist.home.components.PopularTourCard
import com.ahmetkaragunlu.guidemate.screens.tourist.home.model.BestGuideUiModel
import com.ahmetkaragunlu.guidemate.screens.tourist.home.model.PopularToursCardUiModel
import com.ahmetkaragunlu.guidemate.screens.tourist.shared.CategoryItem
import com.ahmetkaragunlu.guidemate.screens.tourist.shared.CategoryCard
import com.ahmetkaragunlu.guidemate.screens.tourist.shared.TourCategory

@Composable
fun TouristHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: TouristHomeViewModel = hiltViewModel(),
) {
    val categories = viewModel.categories
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.spacing_medium)),
        contentPadding = PaddingValues(bottom = dimensionResource(R.dimen.spacing_double_extra_large)),
    ) {
        categoriesSection(
            categories = categories,
            selectedCategory = uiState.selectedCategory,
            onCategoryClick = viewModel::updateSelectedCategory,
        )
        popularToursSection(tours = uiState.popularTours)
        bestGuidesSection(guides = uiState.bestGuides)
    }
}

private fun LazyListScope.categoriesSection(
    categories: List<CategoryItem>,
    selectedCategory: TourCategory,
    onCategoryClick: (TourCategory) -> Unit,
) {
    item {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        ) {
            Text(
                text = stringResource(id = R.string.categories),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier =
                    Modifier.padding(
                        start = dimensionResource(R.dimen.spacing_small),
                        bottom = dimensionResource(R.dimen.spacing_tiny),
                    ),
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
            ) {
                items(categories, key = { it.type }) { category ->
                    CategoryCard(
                        category = category,
                        isSelected = category.type == selectedCategory,
                        onClick = { onCategoryClick(category.type) },
                    )
                }
            }
        }
    }

    item {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
    }
}

private fun LazyListScope.popularToursSection(tours: List<PopularToursCardUiModel>) {
    item {
        Text(
            text = stringResource(id = R.string.popular_experiences),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small)),
        )
    }

    item {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
            contentPadding = PaddingValues(horizontal = 4.dp),
        ) {
            items(tours, key = { it.id }) { tour ->
                PopularTourCard(tour = tour)
            }
        }
    }

    item {
        Spacer(modifier = Modifier.height(20.dp))
    }
}

private fun LazyListScope.bestGuidesSection(guides: List<BestGuideUiModel>) {
    item {
        Text(
            text = stringResource(id = R.string.best_guides_in_region),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small)),
        )
    }

    item {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
    }

    items(guides, key = { it.id }) { guide ->
        BestGuideCard(guide = guide)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
    }
}
