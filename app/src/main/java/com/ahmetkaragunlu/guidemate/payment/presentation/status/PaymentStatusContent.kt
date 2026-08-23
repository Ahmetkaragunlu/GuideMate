package com.ahmetkaragunlu.guidemate.payment.presentation.status

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditButton
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyFromMinorUnit
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentStatusUiModel
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentUiStatus

@Composable
internal fun PaymentStatusContent(
    payment: PaymentStatusUiModel?,
    statusMessage: String?,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val presentation = paymentStatusPresentation(payment?.status)
    val isPaymentSuccessful = payment?.status == PaymentUiStatus.SUCCEEDED
    val shouldShowVersion =
        payment?.status == PaymentUiStatus.VERIFYING

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        top =
                            dimensionResource(
                                if (isPaymentSuccessful) {
                                    R.dimen.spacing_tiny
                                } else {
                                    R.dimen.spacing_large
                                },
                            ),
                        bottom =
                            dimensionResource(
                                if (shouldShowVersion) {
                                    R.dimen.spacing_extra_large
                                } else {
                                    R.dimen.spacing_large
                                },
                            ),
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement =
                if (isPaymentSuccessful) {
                    Arrangement.Top
                } else {
                    Arrangement.Center
                },
        ) {
            if (isPaymentSuccessful) {
                Image(
                    painter = painterResource(R.drawable.onboarding_deal),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
            }

            Column(
                modifier =
                    Modifier
                        .widthIn(max = 380.dp)
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
            ) {
                if (presentation.isLoading) {
                    CircularProgressIndicator(
                        color = colorResource(R.color.brand_color),
                        modifier = Modifier.size(64.dp),
                    )
                } else {
                    Icon(
                        imageVector = presentation.icon,
                        contentDescription = null,
                        tint = presentation.color,
                        modifier =
                            Modifier
                                .size(72.dp)
                                .background(
                                    color = presentation.color.copy(alpha = 0.10f),
                                    shape = CircleShape,
                                )
                                .padding(dimensionResource(R.dimen.spacing_small)),
                    )
                }

                Text(
                    text = stringResource(presentation.titleResId),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = statusMessage ?: stringResource(presentation.descriptionResId),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.text_color),
                    textAlign = TextAlign.Center,
                )

                payment?.let {
                    Text(
                        text = it.amountMinor.toCurrencyFromMinorUnit(it.currencyCode),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.brand_color),
                    )
                    Text(
                        text =
                            stringResource(
                                R.string.payment_reference_format,
                                it.paymentId.take(8).uppercase(),
                            ),
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(R.color.text_color),
                    )
                }

                if (!isPaymentSuccessful) {
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                }

                presentation.primaryActionResId?.let { actionResId ->
                    EditButton(
                        text = actionResId,
                        onClick = onPrimaryAction,
                        modifier =
                            if (isPaymentSuccessful) {
                                Modifier.padding(
                                    top = dimensionResource(R.dimen.spacing_tiny),
                                    bottom = 32.dp,
                                )
                            } else {
                                Modifier
                            },
                    )
                }

                presentation.secondaryActionResId?.let { actionResId ->
                    TextButton(onClick = onSecondaryAction) {
                        Text(
                            text = stringResource(actionResId),
                            color = colorResource(R.color.text_color),
                        )
                    }
                }
            }
        }

        if (shouldShowVersion) {
            Text(
                text = stringResource(R.string.app_version),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_color),
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp),
            )
        }
    }
}
