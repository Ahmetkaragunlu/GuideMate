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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.features.graph.Graph
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.components.ProfileMenuItem
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.components.WalletCard
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.model.menuOptions

@Composable
fun TouristProfileScreen(
    viewModel: TouristProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onNavigateToAccount: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.loadProfileData()
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.spacing_medium))
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
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
            onAddMoneyClick = {},
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

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
}
