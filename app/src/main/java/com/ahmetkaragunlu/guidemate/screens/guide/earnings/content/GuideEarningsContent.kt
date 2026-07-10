package com.ahmetkaragunlu.guidemate.screens.guide.earnings.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.toLocalCurrency
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.components.MonthlyEarningItem
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.model.GuideEarningsUiState
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.model.MonthlyEarningUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.model.toPeriodLabel

@Composable
fun GuideEarningsContent(
    uiState: GuideEarningsUiState,
    modifier: Modifier = Modifier,
) {
    val availableYears =
        (listOf(uiState.currentMonth) + uiState.history)
            .map(MonthlyEarningUiModel::year)
            .distinct()
            .sortedDescending()
    var selectedYear by rememberSaveable { mutableIntStateOf(uiState.currentMonth.year) }
    val selectedYearHistory =
        uiState.history.filter { earning -> earning.year == selectedYear }

    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
        contentPadding =
            PaddingValues(
                top = dimensionResource(R.dimen.spacing_tiny),
                bottom = dimensionResource(R.dimen.spacing_large),
            ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
    ) {
        item {
            CurrentMonthEarningsCard(earning = uiState.currentMonth)
        }

        item {
            EarningsHistoryHeader(
                availableYears = availableYears,
                selectedYear = selectedYear,
                onYearSelected = { year -> selectedYear = year },
            )
        }

        if (selectedYearHistory.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.earnings_empty_year),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.text_color),
                    modifier = Modifier.padding(dimensionResource(R.dimen.spacing_small)),
                )
            }
        } else {
            itemsIndexed(
                items = selectedYearHistory,
                key = { _, earning -> earning.year * 100 + earning.month },
            ) { index, earning ->
                MonthlyEarningItem(
                    earning = earning,
                    showDivider = index < selectedYearHistory.lastIndex,
                )
            }
        }
    }
}

@Composable
private fun EarningsHistoryHeader(
    availableYears: List<Int>,
    selectedYear: Int,
    onYearSelected: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.earnings_history),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.text_color),
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small)),
        )

        EarningsYearSelector(
            availableYears = availableYears,
            selectedYear = selectedYear,
            onYearSelected = onYearSelected,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EarningsYearSelector(
    availableYears: List<Int>,
    selectedYear: Int,
    onYearSelected: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            value = selectedYear.toString(),
            onValueChange = { },
            readOnly = true,
            singleLine = true,
            textStyle = MaterialTheme.typography.labelLarge,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.brand_color),
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                ),
            modifier =
                Modifier
                    .width(112.dp)
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true,
                    ),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            availableYears.forEach { year ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = year.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    onClick = {
                        onYearSelected(year)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun CurrentMonthEarningsCard(
    earning: MonthlyEarningUiModel,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2FD)),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE5E7EB),
        ),
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.spacing_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        ) {
            Text(
                text = stringResource(R.string.this_month_earnings),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.text_color),
            )
            Text(
                text = earning.amount.toLocalCurrency(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF888DED),
            )
            Text(
                text = earning.toPeriodLabel(),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_color),
            )
        }
    }
}
