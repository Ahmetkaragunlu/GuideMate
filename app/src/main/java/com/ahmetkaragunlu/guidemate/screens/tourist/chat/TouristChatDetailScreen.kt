package com.ahmetkaragunlu.guidemate.screens.tourist.chat



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.tourist.chat.model.MessageUiModel

@Composable
fun TouristChatDetailScreen(
    viewModel: TouristChatDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val inputText by viewModel.inputText.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
            reverseLayout = false
        ) {
            item { Spacer(modifier = Modifier.padding(top =dimensionResource(R.dimen.spacing_medium))) }

            items(messages) { message ->
                MessageBubble(message = message)
                Spacer(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_tiny)))
            }

            item { Spacer(modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_medium))) }
        }

        ChatInputArea(
            inputValue = inputText,
            onValueChange = viewModel::onTextChange,
            onSendClick = viewModel::sendMessage
        )
    }
}

@Composable
fun MessageBubble(message: MessageUiModel) {
    val isMe = message.isFromMe

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .background(
                        color = if (isMe) colorResource(R.color.brand_color) else Color(0xFFF2F2F2),
                        shape = RoundedCornerShape(
                            topStart = dimensionResource(R.dimen.radius_medium),
                            topEnd =  dimensionResource(R.dimen.radius_medium),
                            bottomStart = if (isMe)  dimensionResource(R.dimen.radius_medium) else 0.dp,
                            bottomEnd = if (isMe) 0.dp else  dimensionResource(R.dimen.radius_medium)
                        )
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = message.text,
                    color = if (isMe) Color.White else Color.Black,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = message.time,
                style = MaterialTheme.typography.labelSmall,
                color = colorResource(R.color.text_color),
                modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp)
            )
        }
    }
}

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
            placeholder = { Text("Mesaj yazın...", color = colorResource(R.color.text_color)) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.brand_color),
                unfocusedBorderColor = colorResource(R.color.text_color),
                cursorColor = colorResource(R.color.brand_color)
            ),
            maxLines = 4
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))

        IconButton(
            onClick = onSendClick,
            modifier = Modifier
                .size(dimensionResource(R.dimen.spacing_extra_large))
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