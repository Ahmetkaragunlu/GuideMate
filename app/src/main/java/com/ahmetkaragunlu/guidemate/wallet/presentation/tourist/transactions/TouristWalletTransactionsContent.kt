package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.components.TouristWalletTransactionItem
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions.model.TouristWalletTransactionFilter
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions.model.TouristWalletTransactionsUiState

@Composable
fun TouristWalletTransactionsContent(
    uiState: TouristWalletTransactionsUiState,
    onFilterSelected: (TouristWalletTransactionFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyRow(
            modifier =
                Modifier
                    .widthIn(max = 380.dp)
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
            contentPadding =
                PaddingValues(
                    top = dimensionResource(R.dimen.spacing_medium),
                    bottom = dimensionResource(R.dimen.spacing_small),
                ),
        ) {
            items(
                items = TouristWalletTransactionFilter.entries,
                key = { it.name },
            ) { filter ->
                FilterChip(
                    selected = filter == uiState.selectedFilter,
                    onClick = { onFilterSelected(filter) },
                    label = {
                        Text(
                            text = stringResource(filter.titleResId),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    },
                    colors =
                        FilterChipDefaults.filterChipColors(
                            selectedContainerColor =
                                colorResource(R.color.brand_color).copy(alpha = 0.12f),
                            selectedLabelColor = colorResource(R.color.brand_color),
                        ),
                )
            }
        }

        if (uiState.filteredTransactions.isEmpty()) {
            Box(
                modifier =
                    Modifier
                        .widthIn(max = 380.dp)
                        .fillMaxWidth()
                        .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.wallet_transactions_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.text_color),
                )
            }
        } else {
            LazyColumn(
                modifier =
                    Modifier
                        .widthIn(max = 380.dp)
                        .fillMaxWidth()
                        .weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                contentPadding =
                    PaddingValues(
                        top = dimensionResource(R.dimen.spacing_small),
                        bottom = dimensionResource(R.dimen.spacing_large),
                    ),
            ) {
                items(
                    items = uiState.filteredTransactions,
                    key = { it.transactionId },
                ) { transaction ->
                    TouristWalletTransactionItem(transaction = transaction)
                }
            }
        }
    }
}
