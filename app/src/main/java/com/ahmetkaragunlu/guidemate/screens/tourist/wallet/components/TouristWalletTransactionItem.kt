package com.ahmetkaragunlu.guidemate.screens.tourist.wallet.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.formatting.toLocalCurrencyFromMinorUnit
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.TouristWalletTransactionStatus
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.TouristWalletTransactionType
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.TouristWalletTransactionUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun TouristWalletTransactionItem(
    transaction: TouristWalletTransactionUiModel,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
    val isIncoming = transaction.amountMinor >= 0

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacing_medium)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
            ) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text =
                        dateFormatter.format(
                            transaction.createdAt.atZone(ZoneId.systemDefault()),
                        ),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.text_color),
                )
                Text(
                    text = stringResource(transaction.status.titleResId),
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(R.color.text_color),
                )
            }
            Text(
                text =
                    buildString {
                        if (isIncoming) append("+")
                        append(transaction.amountMinor.toLocalCurrencyFromMinorUnit())
                    },
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color =
                    if (isIncoming || transaction.type == TouristWalletTransactionType.REFUND) {
                        Color(0xFF16833B)
                    } else {
                        MaterialTheme.colorScheme.error
                    },
            )
        }
    }
}

private val TouristWalletTransactionStatus.titleResId: Int
    get() =
        when (this) {
            TouristWalletTransactionStatus.PENDING -> R.string.payment_status_pending
            TouristWalletTransactionStatus.COMPLETED -> R.string.payment_status_completed
            TouristWalletTransactionStatus.FAILED -> R.string.payment_status_failed
            TouristWalletTransactionStatus.REFUNDED -> R.string.payment_status_refunded
        }
