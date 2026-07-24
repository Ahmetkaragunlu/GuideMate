package com.ahmetkaragunlu.guidemate.screens.common.selection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.selection.data.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.LanguageOption
import java.util.Locale

@Composable
fun LanguageSelectionBottomSheet(
    isVisible: Boolean,
    selectedLanguageCodes: Set<String>,
    onDismissRequest: () -> Unit,
    onLanguagesSelected: (List<LanguageOption>) -> Unit,
) {
    if (!isVisible) return

    var query by rememberSaveable { mutableStateOf("") }
    var pendingCodes by rememberSaveable { mutableStateOf(selectedLanguageCodes) }
    val locale = LocalConfiguration.current.locales[0] ?: Locale.getDefault()
    val languages =
        remember(locale.toLanguageTag()) { LocaleSelectionCatalog.languages(locale) }
    val filteredLanguages =
        remember(languages, query) {
            languages.filter { it.displayName.contains(query.trim(), ignoreCase = true) }
        }

    SelectionSheetLayout(
        title = stringResource(R.string.select_languages),
        query = query,
        searchPlaceholderResId = R.string.selection_search_language,
        onQueryChange = { query = it },
        onDismissRequest = onDismissRequest,
    ) {
        if (filteredLanguages.isEmpty()) {
            SelectionMessage(R.string.selection_no_results)
        } else {
            LazyColumn(
                modifier =
                    Modifier
                        .weight(weight = 1f, fill = false)
                        .heightIn(max = 460.dp),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
            ) {
                items(
                    items = filteredLanguages,
                    key = LanguageOption::code,
                ) { language ->
                    val isSelected = language.code in pendingCodes
                    SelectionOptionRow(
                        label = language.chipLabel,
                        isSelected = isSelected,
                        onClick = {
                            pendingCodes =
                                if (isSelected) {
                                    pendingCodes - language.code
                                } else {
                                    pendingCodes + language.code
                                }
                        },
                    )
                }
            }
        }
        TextButton(
            onClick = {
                onLanguagesSelected(languages.filter { it.code in pendingCodes })
            },
            enabled = pendingCodes.isNotEmpty(),
            colors =
                ButtonDefaults.textButtonColors(
                    contentColor = colorResource(R.color.brand_color),
                ),
            modifier = Modifier.align(Alignment.End),
        ) {
            Text(
                text = stringResource(R.string.selection_done),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
