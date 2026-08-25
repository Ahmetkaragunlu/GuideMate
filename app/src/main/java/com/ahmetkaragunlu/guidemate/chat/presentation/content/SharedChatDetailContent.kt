package com.ahmetkaragunlu.guidemate.chat.presentation.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.chat.presentation.components.ChatInputArea
import com.ahmetkaragunlu.guidemate.chat.presentation.components.MessageBubble
import com.ahmetkaragunlu.guidemate.chat.presentation.model.MessageUiModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun SharedChatDetailContent(
    messages: List<MessageUiModel>,
    inputText: String,
    canLoadMore: Boolean,
    isLoadingMore: Boolean,
    olderLoadFailed: Boolean,
    onTextChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
    onLoadOlder: () -> Unit,
    onRetryMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.lastOrNull()?.messageId) {
        if (messages.isNotEmpty()) {
            val pagingStateItemCount = if (isLoadingMore || olderLoadFailed) 1 else 0
            listState.animateScrollToItem(messages.size + pagingStateItemCount)
        }
    }

    LaunchedEffect(listState, canLoadMore, isLoadingMore) {
        snapshotFlow {
            listState.isScrollInProgress && listState.firstVisibleItemIndex == 0
        }.distinctUntilChanged()
            .filter { it && canLoadMore && !isLoadingMore }
            .collect { onLoadOlder() }
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

            if (isLoadingMore || olderLoadFailed) {
                item(key = "older-message-state") {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(R.dimen.spacing_small)),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isLoadingMore) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = colorResource(R.color.brand_color),
                                strokeWidth = 2.dp,
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.common_retry),
                                color = colorResource(R.color.brand_color),
                                modifier = Modifier.clickable(onClick = onLoadOlder),
                            )
                        }
                    }
                }
            }

            items(
                items = messages,
                key = MessageUiModel::messageId,
            ) { message ->
                MessageBubble(
                    message = message,
                    onRetry = { onRetryMessage(message.clientMessageId) },
                )
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
