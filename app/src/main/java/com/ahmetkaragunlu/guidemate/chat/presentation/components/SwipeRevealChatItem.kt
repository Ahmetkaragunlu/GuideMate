package com.ahmetkaragunlu.guidemate.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.chat.presentation.model.ChatUiModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Trash
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

private enum class ChatSwipeRevealValue {
    CLOSED,
    REVEALED,
}

@Composable
fun SwipeRevealChatItem(
    chatItem: ChatUiModel,
    isRevealed: Boolean,
    onRevealChanged: (Boolean) -> Unit,
    onChatClick: () -> Unit,
    onClearRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val revealWidth = dimensionResource(R.dimen.spacing_double_extra_large)
    val revealWidthPx = with(LocalDensity.current) { revealWidth.toPx() }
    val revealState =
        remember(chatItem.chatId) {
            AnchoredDraggableState(initialValue = ChatSwipeRevealValue.CLOSED)
        }
    val currentOnRevealChanged by rememberUpdatedState(onRevealChanged)
    val isDeleteActionRevealed = revealState.settledValue == ChatSwipeRevealValue.REVEALED
    val flingBehavior =
        AnchoredDraggableDefaults.flingBehavior(
            state = revealState,
            positionalThreshold = { distance -> distance * REVEAL_POSITIONAL_THRESHOLD },
        )

    SideEffect {
        revealState.updateAnchors(
            DraggableAnchors {
                ChatSwipeRevealValue.CLOSED at 0f
                ChatSwipeRevealValue.REVEALED at -revealWidthPx
            },
        )
    }

    LaunchedEffect(isRevealed) {
        val target =
            if (isRevealed) ChatSwipeRevealValue.REVEALED else ChatSwipeRevealValue.CLOSED
        if (revealState.targetValue != target) revealState.animateTo(target)
    }

    LaunchedEffect(revealState) {
        snapshotFlow { revealState.settledValue }
            .distinctUntilChanged()
            .drop(1)
            .collect { value ->
                currentOnRevealChanged(value == ChatSwipeRevealValue.REVEALED)
            }
    }

    Box(
        modifier = modifier.fillMaxWidth().clipToBounds(),
    ) {
        IconButton(
            onClick = {
                currentOnRevealChanged(false)
                onClearRequest()
            },
            enabled = isDeleteActionRevealed,
            modifier =
                Modifier
                    .align(Alignment.CenterEnd)
                    .width(revealWidth)
                    .graphicsLayer {
                        val currentOffset = revealState.offset.takeUnless(Float::isNaN) ?: 0f
                        alpha = (-currentOffset / revealWidthPx).coerceIn(0f, 1f)
                    },
        ) {
            Icon(
                imageVector = TablerIcons.Trash,
                contentDescription =
                    if (isDeleteActionRevealed) {
                        stringResource(R.string.clear_chat_confirm)
                    } else {
                        null
                    },
                tint = MaterialTheme.colorScheme.error,
            )
        }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .offset {
                        IntOffset(
                            x = revealState.offset.takeUnless(Float::isNaN)?.roundToInt() ?: 0,
                            y = 0,
                        )
                    }
                    .anchoredDraggable(
                        state = revealState,
                        reverseDirection = false,
                        orientation = Orientation.Horizontal,
                        flingBehavior = flingBehavior,
                    ).background(MaterialTheme.colorScheme.background),
        ) {
            ChatListItem(
                chatItem = chatItem,
                onClick = {
                    if (revealState.settledValue == ChatSwipeRevealValue.REVEALED) {
                        currentOnRevealChanged(false)
                    } else {
                        onChatClick()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private const val REVEAL_POSITIONAL_THRESHOLD = 0.35f
