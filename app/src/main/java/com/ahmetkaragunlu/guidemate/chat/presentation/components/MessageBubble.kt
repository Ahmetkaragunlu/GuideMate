package com.ahmetkaragunlu.guidemate.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageDeliveryStatus
import com.ahmetkaragunlu.guidemate.chat.presentation.model.MessageUiModel

@Composable
fun MessageBubble(
    message: MessageUiModel,
    onRetry: () -> Unit,
) {
    val isMe = message.isFromMe
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        Column(horizontalAlignment = if (isMe) Alignment.End else Alignment.Start) {
            Box(
                modifier =
                    Modifier
                        .widthIn(max = 300.dp)
                        .background(
                            color = if (isMe) colorResource(R.color.brand_color) else Color(0xFFF2F2F2),
                            shape =
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isMe) 16.dp else 0.dp,
                                    bottomEnd = if (isMe) 0.dp else 16.dp,
                                ),
                        ).padding(12.dp),
            ) {
                Text(
                    text = message.text,
                    color = if (isMe) Color.White else Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            val statusText =
                when (message.deliveryStatus) {
                    ChatMessageDeliveryStatus.PENDING -> stringResource(R.string.chat_message_sending)
                    ChatMessageDeliveryStatus.FAILED -> stringResource(R.string.chat_message_retry)
                    ChatMessageDeliveryStatus.SENT -> message.time
                }
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelSmall,
                color =
                    if (message.deliveryStatus == ChatMessageDeliveryStatus.FAILED) {
                        MaterialTheme.colorScheme.error
                    } else {
                        Color.Gray
                    },
                modifier =
                    Modifier
                        .padding(
                            top = dimensionResource(R.dimen.spacing_tiny),
                            start = dimensionResource(R.dimen.spacing_tiny),
                            end = dimensionResource(R.dimen.spacing_tiny),
                        )
                        .clickable(
                            enabled = message.deliveryStatus == ChatMessageDeliveryStatus.FAILED,
                            onClick = onRetry,
                        ),
            )
        }
    }
}
