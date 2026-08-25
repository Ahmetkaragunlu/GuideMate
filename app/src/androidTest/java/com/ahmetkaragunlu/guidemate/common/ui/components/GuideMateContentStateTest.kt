package com.ahmetkaragunlu.guidemate.common.ui.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.common.ui.theme.GuideMateTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GuideMateContentStateTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun loading_state_hides_content_and_shows_loading_message() {
        val loadingText = stringResource(R.string.common_loading)

        composeRule.setContent {
            GuideMateTheme(dynamicColor = false) {
                GuideMateContentState(
                    state = ContentLoadState.LOADING,
                    onRetry = {},
                ) {
                    Text(CONTENT_TEXT)
                }
            }
        }

        composeRule.onNodeWithText(loadingText).assertIsDisplayed()
        composeRule.onAllNodesWithText(CONTENT_TEXT).assertCountEquals(0)
    }

    @Test
    fun error_state_retries_only_from_retry_controls() {
        val retryText = stringResource(R.string.common_retry)
        var retryCount = 0

        composeRule.setContent {
            GuideMateTheme(dynamicColor = false) {
                GuideMateContentState(
                    state = ContentLoadState.ERROR,
                    errorMessage = ERROR_TEXT,
                    onRetry = { retryCount++ },
                ) {
                    Text(CONTENT_TEXT)
                }
            }
        }

        composeRule.onNodeWithText(ERROR_TEXT).assertIsDisplayed()
        composeRule.onAllNodesWithText(CONTENT_TEXT).assertCountEquals(0)
        composeRule.onNodeWithContentDescription(retryText).performClick()
        composeRule.runOnIdle { assertEquals(1, retryCount) }
    }

    private fun stringResource(resourceId: Int): String =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(resourceId)

    private companion object {
        const val CONTENT_TEXT = "Loaded content"
        const val ERROR_TEXT = "Network unavailable"
    }
}
