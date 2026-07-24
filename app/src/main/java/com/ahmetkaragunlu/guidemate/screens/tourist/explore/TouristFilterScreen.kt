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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditDropdown
import com.ahmetkaragunlu.guidemate.screens.common.selection.components.CitySelectionBottomSheet
import com.ahmetkaragunlu.guidemate.screens.common.selection.components.CountrySelectionBottomSheet
import com.ahmetkaragunlu.guidemate.screens.common.selection.components.LanguageSelectionBottomSheet
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategory
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategoryUiModel
import com.ahmetkaragunlu.guidemate.screens.tourist.category.TourCategoryCard
import com.ahmetkaragunlu.guidemate.screens.tourist.explore.components.PriceRangeSelector
import com.ahmetkaragunlu.guidemate.screens.tourist.explore.components.RatingBar
import com.ahmetkaragunlu.guidemate.screens.tourist.explore.model.ExploreUiState

@Composable
fun TouristFilterScreen(
    modifier: Modifier = Modifier,
    viewModel: TouristExploreViewModel = hiltViewModel(),
) {
    val categories = viewModel.categories
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var activePicker by rememberSaveable { mutableStateOf<TouristFilterPicker?>(null) }

    TouristFilterContent(
        uiState = uiState,
        categories = categories,
        onCountryClick = { activePicker = TouristFilterPicker.COUNTRY },
        onCityClick = { activePicker = TouristFilterPicker.CITY },
        onCategorySelected = viewModel::updateSelectedCategory,
        onRatingChanged = viewModel::updateSelectedRating,
        onLanguageClick = { activePicker = TouristFilterPicker.LANGUAGE },
        onPriceRangeChange = viewModel::updatePriceRange,
        onClearFilters = viewModel::clearFilters,
        onApplyFilters = { },
        modifier = modifier,
    )

    CountrySelectionBottomSheet(
        isVisible = activePicker == TouristFilterPicker.COUNTRY,
        selectedCountryCode = uiState.selectedCountry?.code,
        onDismissRequest = { activePicker = null },
        onCountrySelected = { country ->
            viewModel.updateSelectedCountry(country)
            activePicker = null
        },
    )

    uiState.selectedCountry?.let { country ->
        CitySelectionBottomSheet(
            isVisible = activePicker == TouristFilterPicker.CITY,
            country = country,
            onDismissRequest = { activePicker = null },
            onCitySelected = { city ->
                viewModel.updateSelectedCity(city)
                activePicker = null
            },
        )
    }

    LanguageSelectionBottomSheet(
        isVisible = activePicker == TouristFilterPicker.LANGUAGE,
        selectedLanguageCodes = uiState.selectedLanguages.mapTo(mutableSetOf()) { it.code },
        onDismissRequest = { activePicker = null },
        onLanguagesSelected = { languages ->
            viewModel.updateSelectedLanguages(languages)
            activePicker = null
        },
    )
}

@Composable
fun TouristFilterContent(
    uiState: ExploreUiState,
    categories: List<TourCategoryUiModel>,
    onCountryClick: () -> Unit,
    onCityClick: () -> Unit,
    onCategorySelected: (TourCategory?) -> Unit,
    onRatingChanged: (Int) -> Unit,
    onLanguageClick: () -> Unit,
    onPriceRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onClearFilters: () -> Unit,
    onApplyFilters: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(R.dimen.spacing_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
    ) {
        EditDropdown(
            value = uiState.selectedCountry?.displayName.orEmpty(),
            placeholder = R.string.select_country,
            onClick = onCountryClick,
        )
        EditDropdown(
            value = uiState.selectedCity?.displayName.orEmpty(),
            placeholder = R.string.select_city,
            enabled = uiState.selectedCountry != null,
            onClick = onCityClick,
        )

        Text(
            text = stringResource(id = R.string.categories),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier =
                Modifier.padding(
                    start = dimensionResource(R.dimen.spacing_small),
                    top = dimensionResource(R.dimen.spacing_tiny),
                ),
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        ) {
            items(categories) { category ->
                TourCategoryCard(
                    category = category,
                    isSelected = category.category == uiState.selectedCategory,
                    onClick = { onCategorySelected(category.category) },
                )
            }
        }

        Text(
            text = stringResource(R.string.rating),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier =
                Modifier.padding(
                    start = dimensionResource(R.dimen.spacing_small),
                    top = dimensionResource(R.dimen.spacing_tiny),
                ),
        )

        RatingBar(
            rating = uiState.selectedRating,
            onRatingChanged = onRatingChanged,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small)),
        )

        Text(
            text = stringResource(R.string.select_languages),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small)),
        )

        EditDropdown(
            value = uiState.selectedLanguages.joinToString { it.displayName },
            placeholder = R.string.language,
            onClick = onLanguageClick,
        )

        Text(
            text = stringResource(R.string.price_range),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small)),
        )

        PriceRangeSelector(
            range = uiState.priceRange,
            onRangeChange = onPriceRangeChange,
            minPrice = 0f,
            maxPrice = 1000f,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = onClearFilters,
                modifier = Modifier.weight(1f),
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                    ),
                border = BorderStroke(width = 1.dp, color = Color(0xFFeeedf1)),
            ) {
                Text(
                    text = stringResource(R.string.clear),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(R.color.brand_color),
                )
            }
            Button(
                onClick = onApplyFilters,
                modifier = Modifier.weight(1f),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.brand_color),
                        contentColor = Color.White,
                    ),
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

private enum class TouristFilterPicker {
    COUNTRY,
    CITY,
    LANGUAGE,
}
