package com.ahmetkaragunlu.guidemate.screens.common.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.ChatUiModel

@Composable
fun ChatListItem(
    chatItem: ChatUiModel,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clickable { onClick() }
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = chatItem.avatarResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(56.dp)
                    .clip(CircleShape),
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_medium)))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = chatItem.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = chatItem.lastMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_color),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = chatItem.time,
                style = MaterialTheme.typography.bodySmall,
                color = if (chatItem.unreadCount > 0) colorResource(R.color.brand_color) else Color.Gray,
                fontWeight = if (chatItem.unreadCount > 0) FontWeight.Bold else FontWeight.Normal,
            )
            Spacer(modifier = Modifier.height(6.dp))
            if (chatItem.unreadCount > 0) {
                Box(
                    modifier =
                        Modifier
                            .size(22.dp)
                            .background(colorResource(R.color.brand_color), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = chatItem.unreadCount.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}
