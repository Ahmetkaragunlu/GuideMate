package com.ahmetkaragunlu.guidemate.screens.guide.wallet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
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
import com.ahmetkaragunlu.guidemate.components.toLocalCurrency
import com.ahmetkaragunlu.guidemate.screens.common.moneyaction.content.MoneyActionBottomSheetContent
import com.ahmetkaragunlu.guidemate.screens.common.moneyaction.model.MoneyActionMethodUi
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.components.MonthlyEarningItem
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.model.MonthlyEarningUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.GuideWalletUiState
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.Transaction
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.components.*

private const val WALLET_EARNINGS_PREVIEW_COUNT = 4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideMyWalletScreen(
    earnings: List<MonthlyEarningUiModel>,
    onNavigateToEarnings: () -> Unit,
    viewModel: GuideMyWalletViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var withdrawAmount by rememberSaveable { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.loadWalletData()
    }

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            viewModel.syncSelectedCardWithDefault()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GuideMyWalletContent(
            uiState = uiState,
            earnings = earnings,
            onWithdrawClick = { showBottomSheet = true },
            onNavigateToEarnings = onNavigateToEarnings,
        )

        if (showBottomSheet) {
            WithdrawBottomSheet(
                sheetState = sheetState,
                amount = withdrawAmount,
                selectedMethod = uiState.selectedMethod,
                availableBalance = uiState.totalBalance,
                onAmountChange = { withdrawAmount = it },
                onChangeMethodClick = viewModel::onChangeBankClick,
                onWithdrawAllClick = {
                    withdrawAmount = uiState.totalBalance.toInt().toString()
                },
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
    uiState: GuideWalletUiState,
    earnings: List<MonthlyEarningUiModel>,
    onWithdrawClick: () -> Unit,
    onNavigateToEarnings: () -> Unit,
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
            maskedIban = uiState.selectedMethod?.subtitle
                ?: stringResource(R.string.default_masked_iban),
        )

        WithdrawButton(onClick = onWithdrawClick)

        EarningsSection(
            items = earnings.take(WALLET_EARNINGS_PREVIEW_COUNT),
            scrollState = earningsScrollState,
            onViewAllClick = onNavigateToEarnings,
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
    items: List<MonthlyEarningUiModel>,
    scrollState: ScrollState,
    onViewAllClick: () -> Unit,
) {
    WalletSection(
        titleResId = R.string.earnings_by_month,
        height = 160.dp,
        scrollState = scrollState,
        headerAction = {
            ViewAllEarningsAction(onClick = onViewAllClick)
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
private fun RecentTransactionsSection(
    items: List<Transaction>,
    scrollState: ScrollState,
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
private fun ViewAllEarningsAction(onClick: () -> Unit) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WithdrawBottomSheet(
    sheetState: SheetState,
    amount: String,
    selectedMethod: MoneyActionMethodUi?,
    availableBalance: Double,
    onAmountChange: (String) -> Unit,
    onChangeMethodClick: () -> Unit,
    onWithdrawAllClick: () -> Unit,
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
            selectedMethod = selectedMethod,
            presetAmounts = listOf(100, 250, 500, 1000),
            onPresetAmountClick = { onAmountChange(it.toString()) },
            onChangeMethodClick = onChangeMethodClick,
            onConfirm = onConfirm,
            extraContent = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.spacing_tiny)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text =
                            stringResource(
                                R.string.available_balance_format,
                                availableBalance.toLocalCurrency(),
                            ),
                        color = colorResource(R.color.text_color),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = stringResource(R.string.withdraw_all),
                        color = colorResource(R.color.brand_color),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier =
                            Modifier
                                .clickable(onClick = onWithdrawAllClick)
                                .padding(start = dimensionResource(R.dimen.spacing_medium)),
                    )
                }
            },
        )
    }
}
