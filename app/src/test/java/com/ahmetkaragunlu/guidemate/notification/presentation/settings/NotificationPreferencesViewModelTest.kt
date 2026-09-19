package com.ahmetkaragunlu.guidemate.notification.presentation.settings

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
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

    @Test
    fun failedUpdatePreservesPreferencesAndReleasesOperationLock() =
        runTest {
            val originalPreferences = defaultNotificationPreferences(chatMessagesEnabled = true)
            val repository =
                FakeNotificationRepository().apply {
                    refreshPreferencesResult = DataResult.Success(originalPreferences)
                    updatePreferencesResult = DataResult.Error(AppError.NoInternet)
                }
            val viewModel = NotificationPreferencesViewModel(repository, FakeResourceProvider())
            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()

            viewModel.updateChatMessages(false)
            runCurrent()

            assertEquals(originalPreferences, viewModel.uiState.value.preferences)
            assertFalse(viewModel.uiState.value.isUpdating)
            assertEquals(
                "string-${R.string.error_no_internet}",
                viewModel.uiState.value.userMessage,
            )
            collection.cancel()
        }
}
