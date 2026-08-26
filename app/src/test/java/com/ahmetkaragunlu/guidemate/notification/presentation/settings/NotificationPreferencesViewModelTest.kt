package com.ahmetkaragunlu.guidemate.notification.presentation.settings

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.defaultNotificationPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationPreferencesViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun initialRefreshPublishesPreferencesAndContentState() =
        runTest {
            val repository = FakeNotificationRepository()
            val viewModel = NotificationPreferencesViewModel(repository, FakeResourceProvider())

            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertEquals(defaultNotificationPreferences(), viewModel.uiState.value.preferences)
            collection.cancel()
        }

    @Test
    fun chatPreferenceUpdateSendsOnlyChangedField() =
        runTest {
            val repository =
                FakeNotificationRepository().apply {
                    updatePreferencesResult =
                        com.ahmetkaragunlu.guidemate.common.result.DataResult.Success(
                            defaultNotificationPreferences(chatMessagesEnabled = false)
                        )
                }
            val viewModel = NotificationPreferencesViewModel(repository, FakeResourceProvider())
            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()

            viewModel.updateChatMessages(false)
            runCurrent()

            assertFalse(repository.lastPreferenceUpdate?.chatMessagesEnabled ?: true)
            assertNull(repository.lastPreferenceUpdate?.upcomingTourRemindersEnabled)
            assertFalse(viewModel.uiState.value.preferences?.chatMessagesEnabled ?: true)
            collection.cancel()
        }
}
