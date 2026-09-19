package com.ahmetkaragunlu.guidemate.notification.presentation

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.testNotification
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun initialRefreshPublishesCanonicalNotificationState() =
        runTest {
            val repository =
                FakeNotificationRepository().apply {
                    notificationState.value = listOf(testNotification())
                    unreadState.value = 1
                    hasMoreState.value = true
                }
            val viewModel = NotificationViewModel(repository, FakeResourceProvider())
            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertEquals("notification-1", viewModel.uiState.value.notifications.single().id)
            assertEquals(1, viewModel.uiState.value.unreadCount)
            assertTrue(viewModel.uiState.value.hasMore)
            collection.cancel()
        }

    @Test
    fun refreshFailureKeepsCachedContentAndExposesRetryMessage() =
        runTest {
            val repository =
                FakeNotificationRepository().apply {
                    notificationState.value = listOf(testNotification())
                    refreshNotificationsResult = DataResult.Error(AppError.NoInternet)
                }
            val viewModel = NotificationViewModel(repository, FakeResourceProvider())
            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertEquals("notification-1", viewModel.uiState.value.notifications.single().id)
            assertNotNull(viewModel.uiState.value.errorMessage)
            collection.cancel()
        }

    @Test
    fun loadMoreGuardsConcurrentAndTerminalRequests() =
        runTest {
            val releaseLoadMore = CompletableDeferred<Unit>()
            val repository =
                FakeNotificationRepository().apply {
                    hasMoreState.value = true
                    loadMoreNotificationsHandler = {
                        releaseLoadMore.await()
                        DataResult.Success(emptyList())
                    }
                }
            val viewModel = NotificationViewModel(repository, FakeResourceProvider())
            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()

            viewModel.loadMore()
            runCurrent()
            assertTrue(viewModel.uiState.value.isLoadingMore)

            viewModel.loadMore()
            runCurrent()
            assertEquals(1, repository.loadMoreNotificationsCalls)

            releaseLoadMore.complete(Unit)
            runCurrent()
            assertFalse(viewModel.uiState.value.isLoadingMore)

            repository.hasMoreState.value = false
            runCurrent()
            viewModel.loadMore()
            runCurrent()
            assertEquals(1, repository.loadMoreNotificationsCalls)
            collection.cancel()
        }

    @Test
    fun readMutationsUpdateOnlyTheRequestedNotificationThenAllNotifications() =
        runTest {
            val repository =
                FakeNotificationRepository().apply {
                    notificationState.value =
                        listOf(
                            testNotification(id = "notification-1"),
                            testNotification(id = "notification-2"),
                        )
                    unreadState.value = 2
                }
            val viewModel = NotificationViewModel(repository, FakeResourceProvider())
            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()

            viewModel.markRead("notification-1")
            runCurrent()

            assertEquals(listOf("notification-1"), repository.markedNotificationIds)
            assertTrue(viewModel.uiState.value.notifications.first().isRead)
            assertFalse(viewModel.uiState.value.notifications.last().isRead)
            assertEquals(1, viewModel.uiState.value.unreadCount)

            repository.markAllReadResult = DataResult.Success(0)
            viewModel.markAllRead()
            runCurrent()

            assertEquals(1, repository.markAllReadCalls)
            assertTrue(viewModel.uiState.value.notifications.all { it.isRead })
            assertEquals(0, viewModel.uiState.value.unreadCount)
            assertFalse(viewModel.uiState.value.isMarkingAllRead)
            collection.cancel()
        }
}
