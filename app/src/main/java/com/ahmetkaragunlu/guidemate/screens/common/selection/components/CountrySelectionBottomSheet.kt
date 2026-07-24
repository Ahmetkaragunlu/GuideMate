package com.ahmetkaragunlu.guidemate.screens.common.selection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.selection.data.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.CountryOption
import java.util.Locale

@Composable
fun CountrySelectionBottomSheet(
    isVisible: Boolean,
    selectedCountryCode: String?,
    onDismissRequest: () -> Unit,
    onCountrySelected: (CountryOption) -> Unit,
) {
    if (!isVisible) return

    var query by rememberSaveable { mutableStateOf("") }
    val locale = LocalConfiguration.current.locales[0] ?: Locale.getDefault()
    val countries =
        remember(locale.toLanguageTag()) { LocaleSelectionCatalog.countries(locale) }
    val filteredCountries =
        remember(countries, query) {
            countries.filter { it.displayName.contains(query.trim(), ignoreCase = true) }
        }

    SelectionSheetLayout(
        title = stringResource(R.string.select_country),
        query = query,
        searchPlaceholderResId = R.string.selection_search_country,
        onQueryChange = { query = it },
        onDismissRequest = onDismissRequest,
    ) {
        if (filteredCountries.isEmpty()) {
            SelectionMessage(R.string.selection_no_results)
        } else {
            LazyColumn(
                modifier = Modifier.heightIn(max = 520.dp),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
            ) {
                items(
                    items = filteredCountries,
                    key = CountryOption::code,
                ) { country ->
                    SelectionOptionRow(
                        label = country.displayName,
                        isSelected = country.code == selectedCountryCode,
                        onClick = { onCountrySelected(country) },
                    )
                }
            }
        }
    }
}
