package com.ahmetkaragunlu.guidemate.screens.tourist.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.MoneyActionBottomSheetContent
import com.ahmetkaragunlu.guidemate.navigation.graph.Graph
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.components.ProfileMenuItem
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.components.WalletCard
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.model.ProfileUiState
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.model.menuOptions
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.saveable.rememberSaveable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TouristProfileScreen(
    viewModel: TouristProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onNavigateToAccount: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.loadProfileData()
    }

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            viewModel.syncSelectedCardWithDefault()
        }
    }

    TouristProfileContent(
        modifier = modifier,
        uiState = uiState,
        onAddMoneyClick = { showBottomSheet = true },
        onNavigateToAccount = onNavigateToAccount,
    )

    if (showBottomSheet) {
        AddMoneyBottomSheet(
            sheetState = sheetState,
            uiState = uiState,
            onAmountChange = viewModel::onDepositAmountChange,
            onPresetAmountClick = viewModel::onDepositPresetSelected,
            onChangeBankClick = viewModel::onChangeBankClick,
            onDismiss = { showBottomSheet = false },
            onConfirm = {
                viewModel.onAddMoneyConfirm()
                showBottomSheet = false
            },
        )
    }
}

@Composable
private fun TouristProfileContent(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState,
    onAddMoneyClick: () -> Unit,
    onNavigateToAccount: (String) -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.spacing_medium))
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        ProfileHeader(
            fullName = uiState.fullName,
            email = uiState.email,
        )

        Spacer(modifier = Modifier.height(24.dp))

        WalletCard(
            balance = uiState.balance,
            onAddMoneyClick = onAddMoneyClick,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

        ProfileMenuSection(onNavigateToAccount = onNavigateToAccount)
    }
}

@Composable
private fun ProfileHeader(
    fullName: String,
    email: String,
) {
    Image(
        painter = painterResource(id = R.drawable.example),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier =
            Modifier
                .size(80.dp)
                .clip(CircleShape),
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = fullName.ifEmpty { stringResource(R.string.profile_default_user) },
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )
    Text(
        text = email,
        color = Color.Gray,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
private fun ProfileMenuSection(
    onNavigateToAccount: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        menuOptions.forEachIndexed { index, option ->
            ProfileMenuItem(
                option = option,
                onClick = {
                    onNavigateToAccount("${Graph.AccountGraph.route}/${option.targetRoute}")
                },
            )
            if (index < menuOptions.lastIndex) {
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = Color.LightGray.copy(alpha = 0.4f),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMoneyBottomSheet(
    sheetState: SheetState,
    uiState: ProfileUiState,
    onAmountChange: (String) -> Unit,
    onPresetAmountClick: (Int) -> Unit,
    onChangeBankClick: () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        MoneyActionBottomSheetContent(
            title = stringResource(R.string.add_money_title),
            amountText = uiState.depositAmount,
            onAmountChange = onAmountChange,
            actionButtonText = stringResource(R.string.profile_add_money),
            helperText = stringResource(R.string.add_money_info_text),
            selectedBank = uiState.selectedBankAccount,
            presetAmounts = listOf(50, 100, 200, 1000),
            onPresetAmountClick = onPresetAmountClick,
            onChangeBankClick = onChangeBankClick,
            onConfirm = { onConfirm() },
        )
    }
}
