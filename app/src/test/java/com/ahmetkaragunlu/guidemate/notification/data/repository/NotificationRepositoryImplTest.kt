package com.ahmetkaragunlu.guidemate.notification.data.repository

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.network.testApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.storage.installation.InstallationIdDataSource
import com.ahmetkaragunlu.guidemate.notification.data.remote.api.NotificationApi
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.NotificationPreferencesResponseDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.NotificationResponseDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.RegisterDeviceRequestDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.UnreadCountResponseDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.UpdateNotificationPreferencesRequestDto
import com.ahmetkaragunlu.guidemate.notification.domain.device.PushInstallationIdProvider
import java.time.Instant
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        val repository = createRepository(api, FakeUserRepository(), backgroundScope)

        repository.refreshNotifications()
        repository.refreshUnreadCount()
        repository.markRead("notification-1")
        repository.markRead("notification-1")

        assertTrue(repository.notifications.value.first { it.notificationId == "notification-1" }.isRead)
        assertEquals(1, repository.unreadCount.value)
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

    private fun createRepository(
        api: NotificationApi,
        userRepository: UserRepository,
        scope: kotlinx.coroutines.CoroutineScope,
    ): NotificationRepositoryImpl {
        val dataStore =
            PreferenceDataStoreFactory.create(
                scope = scope,
                produceFile = { temporaryFolder.newFile("installation-${UUID.randomUUID()}.preferences_pb") },
            )
        return NotificationRepositoryImpl(
            api = api,
            installationIdDataSource = InstallationIdDataSource(dataStore),
            pushInstallationIdProvider =
                object : PushInstallationIdProvider {
                    override suspend fun getId(): String = "firebase-installation-1"
                },
            userRepository = userRepository,
            apiCallExecutor = testApiCallExecutor(),
            applicationScope = scope,
        )
    }

    private class FakeUserRepository(initialState: UserState = UserState()) : UserRepository {
        private val mutableUserState = MutableStateFlow(initialState)
        override val userState: StateFlow<UserState> = mutableUserState

        override suspend fun restoreCachedUser(): UserState = userState.value
    }

    private class FakeNotificationApi(
        private val unreadCount: Long = 0,
    ) : NotificationApi {
        val requestedPages = mutableListOf<Int>()
        var deviceRequest: RegisterDeviceRequestDto? = null

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
