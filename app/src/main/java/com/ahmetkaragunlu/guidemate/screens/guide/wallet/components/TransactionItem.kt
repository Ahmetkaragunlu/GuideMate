package com.ahmetkaragunlu.guidemate.screens.guide.wallet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import com.ahmetkaragunlu.guidemate.components.toLocalCurrency
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.Transaction
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.TransactionType
import compose.icons.TablerIcons
import compose.icons.tablericons.CreditCard
import compose.icons.tablericons.Ticket

@Composable
fun TransactionItem(transaction: Transaction) {
    val isIncome = transaction.type == TransactionType.TOUR_INCOME
    val icon = if (isIncome) TablerIcons.Ticket else TablerIcons.CreditCard
    val amountColor = if (isIncome) Color(0xFF388E3C) else Color(0xFFD32F2F)
    val amountPrefix = if (isIncome) "+" else "-"

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier =
                    Modifier
                        .size(44.dp)
                        .background(Color(0xFFF5F5F5), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.DarkGray,
                    modifier = Modifier.size(24.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = colorResource(R.color.text_color),
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
                Text(
                    text = transaction.formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.text_color),
                )
            }
        }
        Text(
            text = "$amountPrefix${transaction.amount.toLocalCurrency()}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = amountColor,
        )
    }
    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
}
