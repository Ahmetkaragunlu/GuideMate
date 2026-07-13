package com.ahmetkaragunlu.guidemate.screens.common.chat.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.chat.components.ChatInputArea
import com.ahmetkaragunlu.guidemate.screens.common.chat.components.MessageBubble
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.MessageUiModel

@Composable
fun SharedChatDetailContent(
    messages: List<MessageUiModel>,
    inputText: String,
    onTextChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.lastOrNull()?.messageId) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
        ) {
            item { Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium))) }

            items(
                items = messages,
                key = MessageUiModel::messageId,
            ) { message ->
                MessageBubble(message = message)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
            }

            item { Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium))) }
        }

        ChatInputArea(
            inputValue = inputText,
            onValueChange = onTextChanged,
            onSendClick = onSendMessage,
        )
    }
}
