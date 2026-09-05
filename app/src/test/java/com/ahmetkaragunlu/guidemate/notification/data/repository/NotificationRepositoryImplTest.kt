package com.ahmetkaragunlu.guidemate.notification.data.repository

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.network.testApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.storage.installation.InstallationIdDataSource
import com.ahmetkaragunlu.guidemate.notification.data.remote.api.NotificationApi
import com.ahmetkaragunlu.guidemate.notification.data.realtime.NotificationRealtimeClient
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.NotificationPreferencesResponseDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.NotificationResponseDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.MarkRelatedNotificationsReadRequestDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.RegisterDeviceRequestDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.UnreadCountResponseDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.UpdateNotificationPreferencesRequestDto
import com.ahmetkaragunlu.guidemate.notification.domain.device.PushInstallationIdProvider
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetReference
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetType
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.push.SystemNotificationController
import java.time.Instant
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import retrofit2.Response

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
        assertEquals(false, repository.hasMoreNotifications.value)
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

            assertTrue(result is com.ahmetkaragunlu.guidemate.common.result.DataResult.Success)
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

    private fun createRepository(
        api: NotificationApi,
        userRepository: UserRepository,
        scope: kotlinx.coroutines.CoroutineScope,
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

    private class FakeSystemNotificationController : SystemNotificationController {
        val dismissedTargets = mutableListOf<NotificationNavigationTarget>()
        val dismissedReferences = mutableListOf<NotificationTargetReference>()
        var dismissAllCalls = 0

        override fun createChannel() = Unit

        override fun show(target: NotificationNavigationTarget) = Unit

        override fun dismiss(target: NotificationNavigationTarget) {
            dismissedTargets += target
        }

        override fun dismissRelated(target: NotificationTargetReference) {
            dismissedReferences += target
        }

        override fun dismissAll() {
            dismissAllCalls++
        }
    }

    private class FakeNotificationRealtimeClient : NotificationRealtimeClient {
        private val mutableEvents = MutableSharedFlow<Unit>(replay = 1)
        override val events: Flow<Unit> = mutableEvents
        var connectCalls = 0

        override fun connect() {
            connectCalls++
        }

        override fun disconnect() = Unit

        fun emit() {
            mutableEvents.tryEmit(Unit)
        }
    }

    private class FakeUserRepository(initialState: UserState = UserState()) : UserRepository {
        private val mutableUserState = MutableStateFlow(initialState)
        override val userState: StateFlow<UserState> = mutableUserState

        override suspend fun restoreCachedUser(): UserState = userState.value

        override suspend fun updateAvatar(mediaAssetId: String, imageUrl: String) {
            mutableUserState.value =
                mutableUserState.value.copy(avatarMediaId = mediaAssetId, avatarUrl = imageUrl)
        }
    }

    private class FakeNotificationApi(
        private val unreadCount: Long = 0,
    ) : NotificationApi {
        val requestedPages = mutableListOf<Int>()
        var deviceRequest: RegisterDeviceRequestDto? = null
        var relatedReadRequest: MarkRelatedNotificationsReadRequestDto? = null

        override suspend fun getNotifications(
            page: Int,
            size: Int,
        ): Response<ApiPageResponse<NotificationResponseDto>> {
            requestedPages += page
            val content =
                when (page) {
                    0 -> listOf(notification("notification-1", minute = 1, isRead = false))
                    else ->
                        listOf(
                            notification("notification-1", minute = 1, isRead = false),
                            notification("notification-2", minute = 2, isRead = false),
                        )
                }
            return Response.success(
                ApiPageResponse(
                    content = content,
                    page = page,
                    size = size,
                    totalElements = 2,
                    totalPages = 2,
                    isFirst = page == 0,
                    isLast = page > 0,
                ),
            )
        }

        override suspend fun getUnreadCount(): Response<UnreadCountResponseDto> =
            Response.success(UnreadCountResponseDto(unreadCount))

        override suspend fun markRead(
            notificationId: String,
        ): Response<NotificationResponseDto> =
            Response.success(notification(notificationId, minute = 1, isRead = true))

        override suspend fun markAllRead(): Response<UnreadCountResponseDto> =
            Response.success(UnreadCountResponseDto(0))

        override suspend fun markRelatedRead(
            request: MarkRelatedNotificationsReadRequestDto,
        ): Response<UnreadCountResponseDto> {
            relatedReadRequest = request
            return Response.success(UnreadCountResponseDto(unreadCount))
        }

        override suspend fun getPreferences(): Response<NotificationPreferencesResponseDto> =
            Response.success(preferences())

        override suspend fun updatePreferences(
            request: UpdateNotificationPreferencesRequestDto,
        ): Response<NotificationPreferencesResponseDto> = Response.success(preferences())

        override suspend fun registerDevice(
            request: RegisterDeviceRequestDto,
        ): Response<ResponseBody> {
            deviceRequest = request
            return Response.success("".toResponseBody())
        }

        private fun notification(
            id: String,
            minute: Int,
            isRead: Boolean,
        ): NotificationResponseDto =
            NotificationResponseDto(
                id = id,
                type = "CHAT_MESSAGE",
                actorId = 9,
                actorDisplayName = "Guide",
                payload = mapOf("chatId" to "chat-1"),
                isRead = isRead,
                readAt = if (isRead) Instant.parse("2026-08-25T12:03:00Z") else null,
                createdAt = Instant.parse("2026-08-25T12:0${minute}:00Z"),
            )

        private fun preferences(): NotificationPreferencesResponseDto =
            NotificationPreferencesResponseDto(
                upcomingTourRemindersEnabled = true,
                chatMessagesEnabled = true,
                reservationUpdatesEnabled = true,
                reviewRequestsEnabled = true,
                paymentsAndEarningsEnabled = true,
                newReviewsEnabled = true,
                securityAlertsEnabled = true,
            )
    }
}
