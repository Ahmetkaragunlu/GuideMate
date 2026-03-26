package com.ahmetkaragunlu.guidemate.screens.guide.wallet.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import compose.icons.TablerIcons
import compose.icons.tablericons.CreditCard

@Composable
fun WalletCard(
    formattedBalance: String,
    maskedIban: String,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(200.dp),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_extra_large)),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.brand_color)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(24.dp),
        ) {
            Text(
                text = stringResource(R.string.total_withdrawable_balance),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelSmall,
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

            Text(
                text = formattedBalance,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
            )

            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        TablerIcons.CreditCard,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                    Text(
                        text = maskedIban,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Text(
                    "GuideMate",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
