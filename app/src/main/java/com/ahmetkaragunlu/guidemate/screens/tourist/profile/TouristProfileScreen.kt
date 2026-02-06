package com.ahmetkaragunlu.guidemate.screens.tourist.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.model.ProfileMenuOption
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.model.menuOptions
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronRight
import compose.icons.tablericons.Plus



@Composable
fun TouristProfileScreen(
    viewModel: TouristProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.loadProfileData()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.spacing_medium))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        Image(
            painter = painterResource(id = R.drawable.example),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = uiState.fullName.ifEmpty { stringResource(R.string.profile_default_user) },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = uiState.email,
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium,

            )
        Spacer(modifier = Modifier.height(24.dp))

        WalletCard(
            balance = uiState.balance,
            onAddMoneyClick = {
            }
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

        Column(modifier = Modifier.fillMaxWidth()) {
            menuOptions.forEachIndexed { index, option ->
                ProfileMenuItem(
                    option = option,
                    onClick = { viewModel.onMenuItemClicked(option.type) }
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
}


@Composable
fun WalletCard(
    balance: String,
    onAddMoneyClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(R.color.brand_color).copy(alpha = 0.8f))
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.profile_current_balance),
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                        Text(
                            text = balance,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = stringResource(R.string.profile_brand_name),
                        color = Color.White.copy(alpha = 0.95f),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

              Button(
                    onClick = onAddMoneyClick,
                    modifier = Modifier
                        .fillMaxWidth().padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.radius_extra_large)),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.25f),
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                ) {
                    Icon(
                        imageVector = TablerIcons.Plus,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                    Text(
                        text = stringResource(R.string.profile_add_money),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
@Composable
fun ProfileMenuItem(
    option: ProfileMenuOption,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = dimensionResource(R.dimen.spacing_medium), horizontal = dimensionResource(R.dimen.spacing_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = option.icon,
            contentDescription = null,
            tint = Color.Gray,
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_medium)))

        Text(
            text = stringResource(id = option.titleResId),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.text_color)
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = TablerIcons.ChevronRight,
            contentDescription = null,
            tint = Color.LightGray,

        )
    }
}