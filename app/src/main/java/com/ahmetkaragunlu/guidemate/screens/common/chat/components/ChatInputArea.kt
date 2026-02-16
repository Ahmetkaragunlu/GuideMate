package com.ahmetkaragunlu.guidemate.screens.common.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R




@Composable
fun ChatInputArea(
    inputValue: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.spacing_medium)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = inputValue,
            onValueChange = onValueChange,
            placeholder = { Text("Mesaj yazın...", color = Color.Gray) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.brand_color),
                unfocusedBorderColor = Color.LightGray,
                cursorColor = colorResource(R.color.brand_color)
            ),
            maxLines = 4
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
        IconButton(
            onClick = onSendClick,
            modifier = Modifier
                .size(48.dp)
                .background(colorResource(R.color.brand_color), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}