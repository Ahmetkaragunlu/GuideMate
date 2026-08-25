package com.ahmetkaragunlu.guidemate.wallet.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodType
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodUi
import compose.icons.TablerIcons
import compose.icons.tablericons.CreditCard
import compose.icons.tablericons.Refresh

@Composable
internal fun SelectedMethodCard(
    selectedMethod: MoneyActionMethodUi?,
    methodType: MoneyActionMethodType,
    onChangeMethodClick: () -> Unit,
    showChangeMethodAction: Boolean,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(R.dimen.radius_medium)))
                .background(Color(0xFFF0F4F8))
                .padding(dimensionResource(R.dimen.spacing_medium)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector =
                    if (methodType == MoneyActionMethodType.BANK_ACCOUNT) {
                        Icons.Default.AccountBalance
                    } else {
                        TablerIcons.CreditCard
                    },
                contentDescription = null,
                tint = colorResource(R.color.brand_color),
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text =
                        selectedMethod?.title
                            ?: stringResource(
                                if (methodType == MoneyActionMethodType.BANK_ACCOUNT) {
                                    R.string.bank_account_not_selected
                                } else {
                                    R.string.card_not_selected
                                },
                            ),
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text =
                        selectedMethod?.subtitle
                            ?: stringResource(
                                if (methodType == MoneyActionMethodType.BANK_ACCOUNT) {
                                    R.string.default_masked_iban
                                } else {
                                    R.string.no_saved_card
                                },
                            ),
                    color = colorResource(R.color.text_color),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }

        if (showChangeMethodAction) {
            Row(
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable(onClick = onChangeMethodClick)
                        .padding(dimensionResource(R.dimen.spacing_tiny)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    TablerIcons.Refresh,
                    contentDescription = null,
                    tint = colorResource(R.color.brand_color),
                    modifier = Modifier.size(dimensionResource(R.dimen.spacing_medium)),
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
                Text(
                    text = stringResource(R.string.change),
                    color = colorResource(R.color.brand_color),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}
