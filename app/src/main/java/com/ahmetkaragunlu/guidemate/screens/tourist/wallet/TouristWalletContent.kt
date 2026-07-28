package com.ahmetkaragunlu.guidemate.screens.tourist.wallet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.formatting.toLocalCurrencyFromMinorUnit
import com.ahmetkaragunlu.guidemate.screens.tourist.wallet.components.TouristBalanceCard
import com.ahmetkaragunlu.guidemate.screens.tourist.wallet.components.TouristWalletTransactionItem
import com.ahmetkaragunlu.guidemate.screens.tourist.wallet.model.TouristWalletUiState

private const val WALLET_TRANSACTION_PREVIEW_COUNT = 3

@Composable
internal fun TouristWalletContent(
    uiState: TouristWalletUiState,
    onAddMoneyClick: () -> Unit,
    onManageCardsClick: () -> Unit,
    onViewAllTransactionsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val previewTransactions = uiState.transactions.take(WALLET_TRANSACTION_PREVIEW_COUNT)

    LazyColumn(
        modifier = modifier.padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
        contentPadding =
            PaddingValues(
                top = dimensionResource(R.dimen.spacing_medium),
                bottom = dimensionResource(R.dimen.spacing_large),
            ),
    ) {
        item {
            Column(
                modifier =
                    Modifier
                        .widthIn(max = 380.dp)
                        .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
            ) {
                TouristBalanceCard(
                    balance = uiState.balanceMinor.toLocalCurrencyFromMinorUnit(),
                    onAddMoneyClick = onAddMoneyClick,
                )
                DefaultCardSection(
                    uiState = uiState,
                    onManageCardsClick = onManageCardsClick,
                )
                WalletTransactionsHeader(
                    onViewAllClick = onViewAllTransactionsClick,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_small)),
                )
            }
        }

        items(
            items = previewTransactions,
            key = { it.transactionId },
        ) { transaction ->
            TouristWalletTransactionItem(
                transaction = transaction,
                modifier =
                    Modifier
                        .widthIn(max = 380.dp)
                        .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun DefaultCardSection(
    uiState: TouristWalletUiState,
    onManageCardsClick: () -> Unit,
) {
    val defaultCard = uiState.defaultCard

    Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))) {
        SectionTitle(
            icon = Icons.Default.CreditCard,
            title = stringResource(R.string.default_payment_card),
        )
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onManageCardsClick),
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
            color = Color(0xFFF4F7FC),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.spacing_medium)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny))) {
                    Text(
                        text =
                            defaultCard
                                ?.displayName
                                ?: stringResource(R.string.no_saved_card),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text =
                            defaultCard
                                ?.maskedCardNumber
                                ?: stringResource(R.string.add_payment_card_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(R.color.text_color),
                    )
                }
                Text(
                    text = stringResource(R.string.manage),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.brand_color),
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(
    icon: ImageVector,
    title: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorResource(R.color.brand_color),
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun WalletTransactionsHeader(
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SectionTitle(
            icon = Icons.Default.History,
            title = stringResource(R.string.wallet_transaction_history),
        )
        Row(
            modifier =
                Modifier
                    .clickable(onClick = onViewAllClick)
                    .padding(dimensionResource(R.dimen.spacing_tiny)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.view_all),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.brand_color),
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = colorResource(R.color.brand_color),
            )
        }
    }
}
