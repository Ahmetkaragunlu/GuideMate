package com.ahmetkaragunlu.guidemate.profile.presentation.tourist

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.common.ui.image.ImageSourcePicker
import com.ahmetkaragunlu.guidemate.profile.presentation.components.CommonProfileMenuItem
import com.ahmetkaragunlu.guidemate.profile.presentation.components.EditableProfileAvatar
import com.ahmetkaragunlu.guidemate.profile.presentation.tourist.model.ProfileUiState
import com.ahmetkaragunlu.guidemate.profile.presentation.tourist.model.TouristProfileMenuTarget
import com.ahmetkaragunlu.guidemate.profile.presentation.tourist.model.menuOptions
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.components.TouristBalanceCard

@Composable
fun TouristProfileScreen(
    viewModel: TouristProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onNavigateToAccount: (TouristProfileMenuTarget) -> Unit = {},
    onNavigateToWallet: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showPhotoSourceSheet by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val resources = LocalResources.current

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.onUserMessageShown()
        }
    }

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = viewModel::refresh,
        modifier = modifier,
    ) {
        TouristProfileContent(
            modifier = modifier,
            uiState = uiState,
            onProfileImageClick = { showPhotoSourceSheet = true },
            onNavigateToWallet = onNavigateToWallet,
            onNavigateToAccount = onNavigateToAccount,
        )
    }

    ImageSourcePicker(
        isVisible = showPhotoSourceSheet,
        titleResId = R.string.profile_photo_source_title,
        onDismissRequest = { showPhotoSourceSheet = false },
        onImageSelected = viewModel::onProfileImageSelected,
        onError = { errorResId ->
            Toast.makeText(context, resources.getString(errorResId), Toast.LENGTH_LONG).show()
        },
    )
}

@Composable
private fun TouristProfileContent(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState,
    onProfileImageClick: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToAccount: (TouristProfileMenuTarget) -> Unit,
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
            imageUrl = uiState.displayAvatarUrl,
            isAvatarUpdating = uiState.isAvatarUpdating,
            onProfileImageClick = onProfileImageClick,
        )

        Spacer(modifier = Modifier.height(24.dp))

        TouristBalanceCard(
            balance = uiState.balance,
            onWalletClick = onNavigateToWallet,
            onAddMoneyClick = onNavigateToWallet,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

        ProfileMenuSection(onNavigateToAccount = onNavigateToAccount)
    }
}

@Composable
private fun ProfileHeader(
    fullName: String,
    email: String,
    imageUrl: String?,
    isAvatarUpdating: Boolean,
    onProfileImageClick: () -> Unit,
) {
    EditableProfileAvatar(
        imageUrl = imageUrl,
        fallbackImageResId = R.drawable.example,
        isUpdating = isAvatarUpdating,
        size = 80.dp,
        onClick = onProfileImageClick,
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
    onNavigateToAccount: (TouristProfileMenuTarget) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        menuOptions.forEachIndexed { index, option ->
            CommonProfileMenuItem(
                icon = option.icon,
                title = stringResource(id = option.titleResId),
                onClick = { onNavigateToAccount(option.target) },
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
