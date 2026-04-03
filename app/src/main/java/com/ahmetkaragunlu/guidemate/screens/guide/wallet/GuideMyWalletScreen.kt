package com.ahmetkaragunlu.guidemate.screens.guide.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.MoneyActionBottomSheetContent
import com.ahmetkaragunlu.guidemate.components.toLocalCurrency
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideMyWalletScreen(viewModel: GuideMyWalletViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var withdrawAmount by rememberSaveable { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = Modifier.fillMaxSize()) {
        GuideMyWalletContent(
            uiState = uiState,
            onWithdrawClick = { showBottomSheet = true },
        )

        if (showBottomSheet) {
            WithdrawBottomSheet(
                sheetState = sheetState,
                amount = withdrawAmount,
                selectedBank = uiState.selectedBankAccount,
                onAmountChange = { withdrawAmount = it },
                onDismiss = { showBottomSheet = false },
                onConfirm = { amount ->
                    viewModel.withdrawMoney(amount)
                    withdrawAmount = ""
                    showBottomSheet = false
                },
            )
        }
    }
}

@Composable
private fun GuideMyWalletContent(
    uiState: com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.GuideWalletUiState,
    onWithdrawClick: () -> Unit,
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
            formattedBalance = uiState.totalBalance.toLocalCurrency(),
            maskedIban = uiState.selectedBankAccount?.maskedIban ?: "TR** **** ****",
        )

        WithdrawButton(onClick = onWithdrawClick)

        EarningsSection(
            items = uiState.earningSummaries,
            scrollState = earningsScrollState,
        )

        RecentTransactionsSection(
            items = uiState.recentTransactions,
            scrollState = transactionsScrollState,
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
    items: List<com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.EarningSummary>,
    scrollState: androidx.compose.foundation.ScrollState,
) {
    WalletSection(
        titleResId = R.string.earnings_by_month,
        height = 160.dp,
        scrollState = scrollState,
    ) {
        items.forEach { earning ->
            EarningSummaryItem(earning = earning)
        }
    }
}

@Composable
private fun RecentTransactionsSection(
    items: List<com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.Transaction>,
    scrollState: androidx.compose.foundation.ScrollState,
) {
    WalletSection(
        titleResId = R.string.recent_transactions,
        height = 200.dp,
        scrollState = scrollState,
    ) {
        items.forEach { transaction ->
            TransactionItem(transaction = transaction)
        }
    }
}

@Composable
private fun WalletSection(
    titleResId: Int,
    height: Dp,
    scrollState: androidx.compose.foundation.ScrollState,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column {
        Text(
            text = stringResource(titleResId),
            color = colorResource(R.color.brand_color),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WithdrawBottomSheet(
    sheetState: SheetState,
    amount: String,
    selectedBank: com.ahmetkaragunlu.guidemate.screens.common.model.BankAccount?,
    onAmountChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        MoneyActionBottomSheetContent(
            title = stringResource(R.string.withdraw_title),
            amountText = amount,
            onAmountChange = onAmountChange,
            actionButtonText = stringResource(R.string.confirm),
            helperText = stringResource(R.string.withdraw_info_text),
            selectedBank = selectedBank,
            presetAmounts = emptyList(),
            onPresetAmountClick = { },
            onChangeBankClick = { },
            onConfirm = onConfirm,
        )
    }
}
