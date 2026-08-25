package com.ahmetkaragunlu.guidemate.notification.data.repository

import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.coroutines.ApplicationScope
import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.result.mapSuccess
import com.ahmetkaragunlu.guidemate.common.storage.installation.InstallationIdDataSource
import com.ahmetkaragunlu.guidemate.notification.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.notification.data.mapper.toDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.api.NotificationApi
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.RegisterDeviceRequestDto
import com.ahmetkaragunlu.guidemate.notification.domain.device.PushInstallationIdProvider
import com.ahmetkaragunlu.guidemate.notification.domain.model.AppNotification
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferenceUpdate
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferences
import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val NOTIFICATION_PAGE_SIZE = 20

@Singleton
class NotificationRepositoryImpl
@Inject
constructor(
    private val api: NotificationApi,
    private val installationIdDataSource: InstallationIdDataSource,
    private val pushInstallationIdProvider: PushInstallationIdProvider,
    private val userRepository: UserRepository,
    private val apiCallExecutor: ApiCallExecutor,
    @param:ApplicationScope private val applicationScope: CoroutineScope,
) : NotificationRepository {
    private val mutableNotifications = MutableStateFlow<List<AppNotification>>(emptyList())
    override val notifications: StateFlow<List<AppNotification>> = mutableNotifications.asStateFlow()

    private val mutableUnreadCount = MutableStateFlow(0)
    override val unreadCount: StateFlow<Int> = mutableUnreadCount.asStateFlow()

    private val mutablePreferences = MutableStateFlow<NotificationPreferences?>(null)
    override val preferences: StateFlow<NotificationPreferences?> = mutablePreferences.asStateFlow()

    private val mutableHasMoreNotifications = MutableStateFlow(false)
    override val hasMoreNotifications: StateFlow<Boolean> =
        mutableHasMoreNotifications.asStateFlow()

    private val mutablePushEvents = MutableSharedFlow<NotificationNavigationTarget>(
        extraBufferCapacity = 16,
    )
    override val pushEvents: SharedFlow<NotificationNavigationTarget> =
        mutablePushEvents.asSharedFlow()

    private val pageMutex = Mutex()
    private var currentPage = -1

    init {
        observeAuthenticatedUser()
    }

    override suspend fun refreshNotifications(): DataResult<List<AppNotification>> =
        pageMutex.withLock {
            apiCallExecutor.execute(
                request = { api.getNotifications(page = 0, size = NOTIFICATION_PAGE_SIZE) },
                transform = { page -> page.content.map { it.toDomain() } to !page.isLast },
            ).mapSuccess { (notifications, hasMore) ->
                currentPage = 0
                mutableNotifications.value = notifications
                mutableHasMoreNotifications.value = hasMore
                notifications
            }
        }

    override suspend fun loadMoreNotifications(): DataResult<List<AppNotification>> =
        pageMutex.withLock {
            if (!mutableHasMoreNotifications.value) {
                return@withLock DataResult.Success(mutableNotifications.value)
            }
            val nextPage = currentPage + 1
            apiCallExecutor.execute(
                request = {
                    api.getNotifications(page = nextPage, size = NOTIFICATION_PAGE_SIZE)
                },
                transform = { page -> page.content.map { it.toDomain() } to !page.isLast },
            ).mapSuccess { (incoming, hasMore) ->
                currentPage = nextPage
                mutableNotifications.update { current -> mergeNotifications(current, incoming) }
                mutableHasMoreNotifications.value = hasMore
                mutableNotifications.value
            }
        }

    override suspend fun refreshUnreadCount(): DataResult<Int> =
        apiCallExecutor.execute(
            request = api::getUnreadCount,
            transform = { it.unreadCount.toSafeInt() },
        ).mapSuccess { count ->
            mutableUnreadCount.value = count
            count
        }

    override suspend fun markRead(notificationId: String): DataResult<AppNotification> =
        apiCallExecutor.execute(
            request = { api.markRead(notificationId) },
            transform = { it.toDomain() },
        ).mapSuccess { updated ->
            val wasUnread = mutableNotifications.value.any {
                it.notificationId == notificationId && !it.isRead
            }
            mutableNotifications.update { notifications ->
                notifications.map { notification ->
                    if (notification.notificationId == notificationId) updated else notification
                }
            }
            if (wasUnread) {
                mutableUnreadCount.update { count -> (count - 1).coerceAtLeast(0) }
            }
            updated
        }

    override suspend fun markAllRead(): DataResult<Int> =
        apiCallExecutor.execute(
            request = api::markAllRead,
            transform = { it.unreadCount.toSafeInt() },
        ).mapSuccess { unreadCount ->
            mutableNotifications.update { notifications ->
                notifications.map { it.copy(isRead = true) }
            }
            mutableUnreadCount.value = unreadCount
            unreadCount
        }

    override suspend fun refreshPreferences(): DataResult<NotificationPreferences> =
        apiCallExecutor.execute(
            request = api::getPreferences,
            transform = { it.toDomain() },
        ).mapSuccess { preferences ->
            mutablePreferences.value = preferences
            preferences
        }

    override suspend fun updatePreferences(
        update: NotificationPreferenceUpdate,
    ): DataResult<NotificationPreferences> =
        apiCallExecutor.execute(
            request = { api.updatePreferences(update.toDto()) },
            transform = { it.toDomain() },
        ).mapSuccess { preferences ->
            mutablePreferences.value = preferences
            preferences
        }

    override suspend fun registerDevice(): DataResult<Unit> =
        apiCallExecutor.executeUnit {
            api.registerDevice(
                RegisterDeviceRequestDto(
                    installationId = installationIdDataSource.getOrCreate(),
                    firebaseInstallationId = pushInstallationIdProvider.getId(),
                ),
            )
        }

    override fun onPushReceived(target: NotificationNavigationTarget) {
        mutablePushEvents.tryEmit(target)
        if (userRepository.userState.value.isAuthenticated) {
            applicationScope.launch {
                refreshNotifications()
                refreshUnreadCount()
            }
        }
    }

    override fun clearLocalState() {
        currentPage = -1
        mutableNotifications.value = emptyList()
        mutableUnreadCount.value = 0
        mutablePreferences.value = null
        mutableHasMoreNotifications.value = false
    }

    private fun observeAuthenticatedUser() {
        applicationScope.launch {
            userRepository.userState
                .map { it.userId }
                .distinctUntilChanged()
                .collect { userId ->
                    clearLocalState()
                    if (userId != null) registerDevice()
                }
        }
    }
}

private fun mergeNotifications(
    current: List<AppNotification>,
    incoming: List<AppNotification>,
): List<AppNotification> =
    (current + incoming)
        .distinctBy(AppNotification::notificationId)
        .sortedByDescending(AppNotification::createdAt)

private fun Long.toSafeInt(): Int = coerceIn(0, Int.MAX_VALUE.toLong()).toInt()
