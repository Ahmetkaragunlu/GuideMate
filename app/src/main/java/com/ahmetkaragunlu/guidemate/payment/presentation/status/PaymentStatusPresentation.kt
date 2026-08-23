package com.ahmetkaragunlu.guidemate.payment.presentation.status

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentUiStatus

internal data class PaymentStatusPresentation(
    @param:StringRes val titleResId: Int,
    @param:StringRes val descriptionResId: Int,
    @param:StringRes val primaryActionResId: Int?,
    @param:StringRes val secondaryActionResId: Int?,
    val icon: ImageVector,
    val color: Color,
    val isLoading: Boolean,
)

@Composable
internal fun paymentStatusPresentation(status: PaymentUiStatus?): PaymentStatusPresentation =
    when (status) {
        PaymentUiStatus.VERIFYING ->
            PaymentStatusPresentation(
                titleResId = R.string.payment_verifying_title,
                descriptionResId = R.string.payment_verifying_description,
                primaryActionResId = null,
                secondaryActionResId = null,
                icon = Icons.Default.HourglassTop,
                color = colorResource(R.color.brand_color),
                isLoading = true,
            )
        PaymentUiStatus.SUCCEEDED ->
            PaymentStatusPresentation(
                titleResId = R.string.payment_success_title,
                descriptionResId = R.string.payment_success_description,
                primaryActionResId = R.string.payment_done,
                secondaryActionResId = null,
                icon = Icons.Default.CheckCircle,
                color = Color(0xFF16833B),
                isLoading = false,
            )
        PaymentUiStatus.FAILED ->
            PaymentStatusPresentation(
                titleResId = R.string.payment_failed_title,
                descriptionResId = R.string.payment_failed_description,
                primaryActionResId = R.string.payment_retry,
                secondaryActionResId = R.string.payment_exit,
                icon = Icons.Default.Error,
                color = MaterialTheme.colorScheme.error,
                isLoading = false,
            )
        PaymentUiStatus.CANCELLED ->
            PaymentStatusPresentation(
                titleResId = R.string.payment_cancelled_title,
                descriptionResId = R.string.payment_cancelled_description,
                primaryActionResId = R.string.payment_return,
                secondaryActionResId = null,
                icon = Icons.Default.Cancel,
                color = MaterialTheme.colorScheme.error,
                isLoading = false,
            )
        PaymentUiStatus.TIMEOUT ->
            PaymentStatusPresentation(
                titleResId = R.string.payment_timeout_title,
                descriptionResId = R.string.payment_timeout_description,
                primaryActionResId = R.string.payment_retry,
                secondaryActionResId = R.string.payment_exit,
                icon = Icons.Default.HourglassTop,
                color = Color(0xFFB7791F),
                isLoading = false,
            )
        PaymentUiStatus.REFUND_PENDING ->
            PaymentStatusPresentation(
                titleResId = R.string.payment_refund_pending_title,
                descriptionResId = R.string.payment_refund_pending_description,
                primaryActionResId = R.string.payment_return,
                secondaryActionResId = null,
                icon = Icons.Default.HourglassTop,
                color = Color(0xFFB7791F),
                isLoading = false,
            )
        PaymentUiStatus.REFUNDED ->
            PaymentStatusPresentation(
                titleResId = R.string.payment_refunded_title,
                descriptionResId = R.string.payment_refunded_description,
                primaryActionResId = R.string.payment_return,
                secondaryActionResId = null,
                icon = Icons.Default.CheckCircle,
                color = Color(0xFF16833B),
                isLoading = false,
            )
        PaymentUiStatus.MANUAL_REVIEW ->
            PaymentStatusPresentation(
                titleResId = R.string.payment_manual_review_title,
                descriptionResId = R.string.payment_manual_review_description,
                primaryActionResId = R.string.payment_return,
                secondaryActionResId = null,
                icon = Icons.Default.HourglassTop,
                color = Color(0xFFB7791F),
                isLoading = false,
            )
        null ->
            PaymentStatusPresentation(
                titleResId = R.string.payment_not_found_title,
                descriptionResId = R.string.payment_not_found_description,
                primaryActionResId = R.string.payment_return_home,
                secondaryActionResId = null,
                icon = Icons.Default.Error,
                color = MaterialTheme.colorScheme.error,
                isLoading = false,
            )
    }
