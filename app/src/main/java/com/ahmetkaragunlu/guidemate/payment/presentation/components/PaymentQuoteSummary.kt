package com.ahmetkaragunlu.guidemate.payment.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyFromMinorUnit
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote

@Composable
fun PaymentQuoteSummary(
    quote: PaymentQuote,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFF4F7FC),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
                ).padding(dimensionResource(R.dimen.spacing_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.payment_charge_amount),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_color),
            )
            Text(
                text =
                    quote.chargeAmountMinor.toCurrencyFromMinorUnit(
                        quote.chargeCurrencyCode,
                    ),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.brand_color),
            )
        }
        Text(
            text = stringResource(R.string.payment_quote_notice),
            style = MaterialTheme.typography.bodySmall,
            color = colorResource(R.color.text_color),
        )
    }
}

