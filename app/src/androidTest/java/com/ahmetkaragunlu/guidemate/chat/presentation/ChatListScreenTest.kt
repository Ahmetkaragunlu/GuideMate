package com.ahmetkaragunlu.guidemate.chat.presentation

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.platform.app.InstrumentationRegistry
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.chat.presentation.model.ChatListUiState
import com.ahmetkaragunlu.guidemate.chat.presentation.model.ChatUiModel
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.common.ui.theme.GuideMateTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ChatListScreenTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun swipe_reveals_delete_action_without_dismissing_chat_and_cancel_closes_action() {
        var clearRequestCount = 0

        composeRule.setContent {
            GuideMateTheme(dynamicColor = false) {
                ChatListScreen(
                    uiState =
                        ChatListUiState(
                            chats = listOf(chat()),
                            loadState = ContentLoadState.CONTENT,
                        ),
                    onNavigateToDetail = {},
                    onRetry = {},
                    onClearChat = { clearRequestCount++ },
                    onMessageShown = {},
                )
            }
        }

        composeRule.onNodeWithText(CHAT_NAME).performTouchInput { swipeLeft() }
        composeRule.waitForIdle()

        composeRule.onNodeWithText(CHAT_NAME).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(stringResource(R.string.clear_chat_confirm)).assertIsDisplayed().performClick()
        composeRule.onNodeWithText(stringResource(R.string.clear_chat_title)).assertIsDisplayed()

        composeRule.onNodeWithText(stringResource(R.string.clear_chat_cancel)).performClick()

        composeRule
            .onAllNodesWithText(stringResource(R.string.clear_chat_title))
            .assertCountEquals(0)
        composeRule
            .onAllNodesWithContentDescription(stringResource(R.string.clear_chat_confirm))
            .assertCountEquals(0)
        composeRule.runOnIdle { assertEquals(0, clearRequestCount) }
    }

    @Test
    fun revealing_another_chat_closes_previous_delete_action() {
        val deleteDescription = stringResource(R.string.clear_chat_confirm)

        composeRule.setContent {
            GuideMateTheme(dynamicColor = false) {
                ChatListScreen(
                    uiState =
                        ChatListUiState(
                            chats =
                                listOf(
                                    chat(chatId = "chat-1", name = CHAT_NAME),
                                    chat(chatId = "chat-2", name = SECOND_CHAT_NAME),
                                ),
                            loadState = ContentLoadState.CONTENT,
                        ),
                    onNavigateToDetail = {},
                    onRetry = {},
                    onClearChat = {},
                    onMessageShown = {},
                )
            }
        }

        composeRule.onNodeWithText(CHAT_NAME).performTouchInput { swipeLeft() }
        composeRule.waitForIdle()
        composeRule.onAllNodesWithContentDescription(deleteDescription).assertCountEquals(1)

        composeRule.onNodeWithText(SECOND_CHAT_NAME).performTouchInput { swipeLeft() }
        composeRule.waitForIdle()

        composeRule.onAllNodesWithContentDescription(deleteDescription).assertCountEquals(1)
        composeRule.onNodeWithText(CHAT_NAME).assertIsDisplayed()
        composeRule.onNodeWithText(SECOND_CHAT_NAME).assertIsDisplayed()
    }

    private fun chat(
        chatId: String = "chat-1",
        name: String = CHAT_NAME,
    ) =
        ChatUiModel(
            chatId = chatId,
            remoteUserId = 2L,
            name = name,
            lastMessage = "Merhaba",
            time = "12:30",
            avatarResId = R.drawable.ic_default_avatar,
        )

    private fun stringResource(resourceId: Int): String =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(resourceId)

    private companion object {
        const val CHAT_NAME = "Ayse Kaya"
        const val SECOND_CHAT_NAME = "Mehmet Demir"
    }
}
