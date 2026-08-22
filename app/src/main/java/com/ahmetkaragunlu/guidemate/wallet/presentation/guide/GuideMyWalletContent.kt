package com.ahmetkaragunlu.guidemate.wallet.presentation.guide

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyFromMinorUnit
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.components.MonthlyEarningItem
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.model.MonthlyEarningUiModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.components.WalletCard
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.components.WalletTransactionItem
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.components.simpleVerticalScrollbar
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.GuideWalletUiState
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionUiModel

private const val WALLET_EARNINGS_PREVIEW_COUNT = 4
private const val WALLET_TRANSACTION_PREVIEW_COUNT = 3

@Composable
internal fun GuideMyWalletContent(
    uiState: GuideWalletUiState,
    earnings: List<MonthlyEarningUiModel>,
    onWithdrawClick: () -> Unit,
    onNavigateToEarnings: () -> Unit,
    onNavigateToTransactions: () -> Unit,
) {
    val screenScrollState = rememberScrollState()
    val earningsScrollState = rememberScrollState()
    val transactionsScrollState = rememberScrollState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                .verticalScroll(screenScrollState),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

        WalletCard(
            formattedBalance =
                uiState.availableBalanceMinor.toCurrencyFromMinorUnit(uiState.currencyCode),
            maskedIban = uiState.defaultMethod?.subtitle
                ?: stringResource(R.string.default_masked_iban),
        )

        WithdrawButton(onClick = onWithdrawClick)

        EarningsSection(
            items = earnings.take(WALLET_EARNINGS_PREVIEW_COUNT),
            scrollState = earningsScrollState,
            onViewAllClick = onNavigateToEarnings,
        )

        RecentFinancialTransactionsSection(
            items = uiState.recentTransactions.take(WALLET_TRANSACTION_PREVIEW_COUNT),
            scrollState = transactionsScrollState,
            onViewAllClick = onNavigateToTransactions,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
    }
}

@Composable
private fun WithdrawButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_tiny)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.brand_color)),
    ) {
        Text(
            text = stringResource(R.string.withdraw_money),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun EarningsSection(
    items: List<MonthlyEarningUiModel>,
    scrollState: ScrollState,
    onViewAllClick: () -> Unit,
) {
    WalletSection(
        titleResId = R.string.earnings_by_month,
        height = 160.dp,
        scrollState = scrollState,
        headerAction = {
            ViewAllAction(onClick = onViewAllClick)
        },
    ) {
        items.forEachIndexed { index, earning ->
            MonthlyEarningItem(
                earning = earning,
                showDivider = index < items.lastIndex,
            )
        }
    }
}

@Composable
private fun RecentFinancialTransactionsSection(
    items: List<WalletTransactionUiModel>,
    scrollState: ScrollState,
    onViewAllClick: () -> Unit,
) {
    WalletSection(
        titleResId = R.string.recent_transactions,
        height = 200.dp,
        scrollState = scrollState,
        headerAction = {
            ViewAllAction(onClick = onViewAllClick)
        },
    ) {
        items.forEach { transaction ->
            WalletTransactionItem(transaction = transaction)
        }
    }
}

@Composable
private fun WalletSection(
    titleResId: Int,
    height: Dp,
    scrollState: ScrollState,
    headerAction: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(titleResId),
                color = colorResource(R.color.brand_color),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            headerAction?.invoke()
        }
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(height)
                    .simpleVerticalScrollbar(scrollState),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(end = 12.dp),
                content = content,
            )
        }
    }
}

@Composable
private fun ViewAllAction(onClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .clickable(onClick = onClick)
                .padding(dimensionResource(R.dimen.spacing_tiny)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.view_all),
            style = MaterialTheme.typography.labelLarge,
            color = colorResource(R.color.brand_color),
            fontWeight = FontWeight.Bold,
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = colorResource(R.color.brand_color),
        )
    }
}
