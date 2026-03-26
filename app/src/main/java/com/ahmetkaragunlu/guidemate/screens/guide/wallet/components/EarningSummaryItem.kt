package com.ahmetkaragunlu.guidemate.screens.guide.wallet.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.toLocalCurrency
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.EarningSummary

@Composable
fun EarningSummaryItem(earning: EarningSummary) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { /* TODO: Detaya git */ }
                .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = earning.periodName,
            style = MaterialTheme.typography.titleSmall,
            color = colorResource(R.color.text_color),
            fontWeight = FontWeight.Bold,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "+${earning.amount.toLocalCurrency()}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF388E3C),
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
}
