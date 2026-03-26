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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.toLocalCurrency
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideMyWalletScreen(viewModel: GuideMyWalletViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val earningsScrollState = rememberScrollState()
    val transactionsScrollState = rememberScrollState()

    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

            WalletCard(
                formattedBalance = uiState.totalBalance.toLocalCurrency(),
                maskedIban = uiState.selectedBankAccount?.maskedIban ?: "TR** **** ****",
            )

            Button(
                onClick = { showBottomSheet = true },
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

            Column {
                Text(
                    text = stringResource(R.string.earnings_by_month),
                    color = colorResource(R.color.brand_color),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .simpleVerticalScrollbar(earningsScrollState),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .verticalScroll(earningsScrollState)
                                .padding(end = 12.dp),
                    ) {
                        uiState.earningSummaries.forEach { earning ->
                            EarningSummaryItem(earning = earning)
                        }
                    }
                }
            }

            Column {
                Text(
                    text = stringResource(R.string.recent_transactions),
                    color = colorResource(R.color.brand_color),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .simpleVerticalScrollbar(transactionsScrollState),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .verticalScroll(transactionsScrollState)
                                .padding(end = 12.dp),
                    ) {
                        uiState.recentTransactions.forEach { transaction ->
                            TransactionItem(transaction = transaction)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color.White,
                dragHandle = { BottomSheetDefaults.DragHandle() },
            ) {
                WithdrawBottomSheetContent(
                    availableBalance = uiState.totalBalance,
                    selectedBank = uiState.selectedBankAccount,
                    onConfirm = { amount ->
                        viewModel.withdrawMoney(amount)
                        showBottomSheet = false
                    },
                )
            }
        }
    }
}
