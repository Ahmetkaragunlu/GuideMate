package com.ahmetkaragunlu.guidemate.notification.data.repository

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.network.testApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.storage.installation.InstallationIdDataSource
import com.ahmetkaragunlu.guidemate.notification.data.remote.api.NotificationApi
import com.ahmetkaragunlu.guidemate.notification.data.realtime.NotificationRealtimeClient
import com.ahmetkaragunlu.guidemate.notification.domain.device.PushInstallationIdProvider
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetReference
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetType
import com.ahmetkaragunlu.guidemate.testing.auth.FakeUserRepository
import java.util.UUID
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationRepositoryImplTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    @Test
    fun `pagination merges duplicate notifications in newest first order`() = runTest {
        val api = FakeNotificationApi()
        val repository = createRepository(api, FakeUserRepository(), backgroundScope)

        repository.refreshNotifications()
        repository.loadMoreNotifications()

        assertEquals(listOf("notification-2", "notification-1"), repository.notifications.value.map { it.notificationId })
        assertFalse(repository.hasMoreNotifications.value)
        assertEquals(listOf(0, 1), api.requestedPages)
    }

    @Test
    fun `mark read updates cached item and decrements unread count once`() = runTest {
        val api = FakeNotificationApi(unreadCount = 2)
        val systemNotifications = FakeSystemNotificationController()
        val repository =
            createRepository(
                api,
                FakeUserRepository(),
                backgroundScope,
                systemNotifications = systemNotifications,
            )

        repository.refreshNotifications()
        repository.refreshUnreadCount()
        repository.markRead("notification-1")
        repository.markRead("notification-1")

        assertTrue(repository.notifications.value.first { it.notificationId == "notification-1" }.isRead)
        assertEquals(1, repository.unreadCount.value)
        assertEquals("notification-1", systemNotifications.dismissedTargets.last().notificationId)
    }

    @Test
    fun `in flight refresh completes before a later read mutation updates canonical state`() =
        runTest {
            val api = FakeNotificationApi(unreadCount = 2)
            val repository = createRepository(api, FakeUserRepository(), backgroundScope)
            runCurrent()
            repository.refreshNotifications()
            repository.refreshUnreadCount()

            api.notificationRequestStarted = CompletableDeferred()
            api.notificationResponseGate = CompletableDeferred()
            val refresh = async { repository.refreshNotifications() }
            api.notificationRequestStarted?.await()
            val markRead = async { repository.markRead("notification-1") }
            runCurrent()

            assertEquals(0, api.markReadCalls)

            api.notificationResponseGate?.complete(Unit)
            refresh.await()
            markRead.await()

            assertTrue(repository.notifications.value.single().isRead)
            assertEquals(1, repository.unreadCount.value)
        }

    @Test
    fun `mark related read applies canonical count and updates only matching cached items`() =
        runTest {
            val api = FakeNotificationApi(unreadCount = 1)
            val systemNotifications = FakeSystemNotificationController()
            val repository =
                createRepository(
                    api,
                    FakeUserRepository(),
                    backgroundScope,
                    systemNotifications = systemNotifications,
                )
            repository.refreshNotifications()

            val result =
                repository.markRelatedRead(
                    NotificationTargetReference(NotificationTargetType.CHAT, "chat-1"),
                )

            assertTrue(result is DataResult.Success)
            assertEquals(1, repository.unreadCount.value)
            assertTrue(repository.notifications.value.first().isRead)
            assertEquals("CHAT", api.relatedReadRequest?.targetType)
            assertEquals("chat-1", api.relatedReadRequest?.targetId)
            assertEquals(
                NotificationTargetReference(NotificationTargetType.CHAT, "chat-1"),
                systemNotifications.dismissedReferences.single(),
            )
        }

    @Test
    fun `mark all read clears system notifications`() = runTest {
        val systemNotifications = FakeSystemNotificationController()
        val repository =
            createRepository(
                FakeNotificationApi(unreadCount = 0),
                FakeUserRepository(),
                backgroundScope,
                systemNotifications = systemNotifications,
            )

        repository.markAllRead()

        assertEquals(1, systemNotifications.dismissAllCalls)
    }

    @Test
    fun `authenticated user registers installation and firebase identifiers`() = runTest {
        val api = FakeNotificationApi()
        val userRepository =
            FakeUserRepository(
                UserState(userId = 7, email = "user@example.com"),
            )
        createRepository(api, userRepository, backgroundScope)

        runCurrent()
        advanceUntilIdle()

        val request = api.deviceRequest
        assertNotNull(request)
        UUID.fromString(request?.installationId)
        assertEquals("firebase-installation-1", request?.firebaseInstallationId)
    }

    @Test
    fun `firebase callback identifier is registered for authenticated user`() = runTest {
        val api = FakeNotificationApi()
        val userRepository =
            FakeUserRepository(
                UserState(userId = 7, email = "user@example.com"),
            )
        val repository = createRepository(api, userRepository, backgroundScope)

        runCurrent()
        advanceUntilIdle()
        repository.registerDevice("firebase-callback-installation")

        assertEquals("firebase-callback-installation", api.deviceRequest?.firebaseInstallationId)
    }

    @Test
    fun `firebase callback does not register device before authentication`() = runTest {
        val api = FakeNotificationApi()
        val repository = createRepository(api, FakeUserRepository(), backgroundScope)

        repository.registerDevice("firebase-callback-installation")

        assertEquals(null, api.deviceRequest)
    }

    @Test
    fun `realtime event refreshes canonical notifications and unread count`() = runTest {
        val scope = CoroutineScope(SupervisorJob() + UnconfinedTestDispatcher(testScheduler))
        try {
            val api = FakeNotificationApi(unreadCount = 4)
            val realtimeClient = FakeNotificationRealtimeClient()
            val userRepository =
                FakeUserRepository(
                    UserState(userId = 7, email = "user@example.com"),
                )
            val repository = createRepository(api, userRepository, scope, realtimeClient)

            realtimeClient.emit()
            runCurrent()

            assertEquals(listOf("notification-1"), repository.notifications.value.map { it.notificationId })
            assertEquals(4, repository.unreadCount.value)
            assertTrue(realtimeClient.connectCalls > 0)
        } finally {
            scope.cancel()
        }
    }

    @Test
    fun `delayed old account response cannot overwrite notifications loaded for new account`() =
        runTest {
            val api = FakeNotificationApi()
            val userRepository =
                FakeUserRepository(UserState(userId = 1, email = "old@example.com"))
            val repository = createRepository(api, userRepository, backgroundScope)
            runCurrent()

            api.firstPageNotificationId = "old-notification"
            api.notificationRequestStarted = CompletableDeferred()
            api.notificationResponseGate = CompletableDeferred()
            val oldRefresh = async { repository.refreshNotifications() }
            api.notificationRequestStarted?.await()

            userRepository.state.value = UserState(userId = 2, email = "new@example.com")
            api.firstPageNotificationId = "new-notification"
            runCurrent()
            val newRefresh = async { repository.refreshNotifications() }
            runCurrent()

            api.notificationResponseGate?.complete(Unit)
            oldRefresh.await()
            newRefresh.await()

            assertEquals(
                listOf("new-notification"),
                repository.notifications.value.map { it.notificationId },
            )
        }

    private fun createRepository(
        api: NotificationApi,
        userRepository: UserRepository,
        scope: CoroutineScope,
        realtimeClient: NotificationRealtimeClient = FakeNotificationRealtimeClient(),
        systemNotifications: FakeSystemNotificationController = FakeSystemNotificationController(),
    ): NotificationRepositoryImpl {
        val dataStore =
            PreferenceDataStoreFactory.create(
                scope = scope,
                produceFile = { temporaryFolder.newFile("installation-${UUID.randomUUID()}.preferences_pb") },
            )
        return NotificationRepositoryImpl(
            api = api,
            realtimeClient = realtimeClient,
            installationIdDataSource = InstallationIdDataSource(dataStore),
            pushInstallationIdProvider =
                object : PushInstallationIdProvider {
                    override suspend fun registerAndGetId(): String = "firebase-installation-1"
                },
            userRepository = userRepository,
            apiCallExecutor = testApiCallExecutor(),
            systemNotificationController = systemNotifications,
            applicationScope = scope,
        )
    }
}
