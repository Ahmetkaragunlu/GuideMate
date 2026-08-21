package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toPlatformCurrencyFromMinorUnit
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.model.MonthlyEarningUiModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.model.toPeriodLabel

@Composable
fun MonthlyEarningItem(
    earning: MonthlyEarningUiModel,
    showDivider: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_medium)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = earning.toPeriodLabel(),
            style = MaterialTheme.typography.titleSmall,
            color = colorResource(R.color.text_color),
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "+${earning.amountMinor.toPlatformCurrencyFromMinorUnit()}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF388E3C),
        )
    }

    if (showDivider) {
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.LightGray.copy(alpha = 0.5f),
        )
    }
}
