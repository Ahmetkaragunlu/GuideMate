package com.ahmetkaragunlu.guidemate.screens.common.selection.components

import android.widget.ImageView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.CityOption
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.CitySearchResult
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.CountryOption
import com.ahmetkaragunlu.guidemate.screens.common.selection.viewmodel.CityPickerViewModel

@Composable
fun CitySelectionBottomSheet(
    isVisible: Boolean,
    country: CountryOption,
    onDismissRequest: () -> Unit,
    onCitySelected: (CityOption) -> Unit,
    viewModel: CityPickerViewModel = hiltViewModel(),
) {
    if (!isVisible) return

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(country.code) { viewModel.start(country.code) }
    LaunchedEffect(uiState.selectedCity) {
        uiState.selectedCity?.let { city ->
            onCitySelected(city)
            viewModel.consumeSelection()
        }
    }

    SelectionSheetLayout(
        title = stringResource(R.string.selection_city_title, country.displayName),
        query = uiState.query,
        searchPlaceholderResId = R.string.selection_search_city,
        onQueryChange = viewModel::onQueryChange,
        onDismissRequest = onDismissRequest,
    ) {
        CitySelectionContent(
            query = uiState.query,
            results = uiState.results,
            isLoading = uiState.isLoading || uiState.isResolvingSelection,
            hasError = uiState.hasError,
            onResultSelected = viewModel::onResultSelected,
            onRetry = viewModel::retry,
        )
        GoogleAttribution()
    }
}

@Composable
private fun CitySelectionContent(
    query: String,
    results: List<CitySearchResult>,
    isLoading: Boolean,
    hasError: Boolean,
    onResultSelected: (CitySearchResult) -> Unit,
    onRetry: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = 220.dp, max = 460.dp),
        contentAlignment = Alignment.Center,
    ) {
        when {
            isLoading -> CircularProgressIndicator(color = colorResource(R.color.brand_color))
            hasError -> SelectionError(onRetry = onRetry)
            query.trim().length < 2 -> SelectionMessage(R.string.selection_city_query_hint)
            results.isEmpty() -> SelectionMessage(R.string.selection_no_results)
            else ->
                LazyColumn(
                    modifier = Modifier.matchParentSize(),
                    verticalArrangement =
                        Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
                ) {
                    items(
                        items = results,
                        key = CitySearchResult::placeId,
                    ) { result ->
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .clickable { onResultSelected(result) }
                                    .padding(vertical = dimensionResource(R.dimen.spacing_small)),
                        ) {
                            Text(
                                text = result.primaryText,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                            )
                            if (result.secondaryText.isNotBlank()) {
                                Text(
                                    text = result.secondaryText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorResource(R.color.text_color),
                                )
                            }
                        }
                    }
                }
        }
    }
}

@Composable
private fun SelectionError(onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
    ) {
        SelectionMessage(R.string.selection_city_error)
        TextButton(onClick = onRetry) {
            Text(
                text = stringResource(R.string.selection_retry),
                color = colorResource(R.color.brand_color),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun GoogleAttribution() {
    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                setImageResource(R.drawable.powered_by_google_attribution)
                contentDescription = context.getString(R.string.selection_powered_by_google)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
            }
        },
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.spacing_small))
                .height(18.dp),
    )
}
