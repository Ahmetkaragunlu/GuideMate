package com.ahmetkaragunlu.guidemate.screens.tourist.explore

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.ahmetkaragunlu.guidemate.components.EditDropdown
import com.ahmetkaragunlu.guidemate.screens.tourist.explore.components.PriceRangeSelector
import com.ahmetkaragunlu.guidemate.screens.tourist.explore.components.RatingBar
import com.ahmetkaragunlu.guidemate.screens.tourist.shared.CategoryCard

@Composable
fun TouristFilterScreen(
    modifier: Modifier = Modifier,
    viewModel: TouristExploreViewModel = hiltViewModel()
) {
    val categories = viewModel.categories
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(dimensionResource(R.dimen.spacing_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        EditDropdown(
            value = "",
            placeholder = R.string.select_country
        )
        EditDropdown(
            value = "",
            placeholder = R.string.select_city
        )

        Text(
            text = stringResource(id = R.string.categories),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.spacing_small),
                top = dimensionResource(R.dimen.spacing_tiny)
            )
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
        ) {
            items(categories) { category ->
                CategoryCard(
                    category = category,
                    isSelected = (category.type == uiState.selectedCategory),
                    onClick = {
                        viewModel.updateSelectedCategory(category.type)
                    }
                )
            }
        }

        Text(
            text = stringResource(R.string.rating),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.spacing_small),
                top = dimensionResource(R.dimen.spacing_tiny)
            )
        )

        RatingBar(
            rating = uiState.selectedRating,
            onRatingChanged = { newRating ->
                viewModel.updateSelectedRating(newRating)
            },
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small))
        )

        Text(
            text = stringResource(R.string.select_languages),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small))
        )

        EditDropdown(
            value = "",
            placeholder = R.string.language
        )

        Text(
            text = stringResource(R.string.price_range),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small))
        )

        PriceRangeSelector(
            range = uiState.priceRange,
            onRangeChange = { newRange -> viewModel.updatePriceRange(newRange) },
            minPrice = 0f,
            maxPrice = 1000f
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = {  },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                  containerColor = Color.White,

                ),
                border = BorderStroke(width = 1.dp, color = Color(0xFFeeedf1))
            ) {
                Text(
                    text = stringResource(R.string.clear),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(R.color.brand_color)

                )
            }
            Button(
                onClick = { },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.brand_color),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.apply_filter),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,

                )
            }
        }
    }
}

