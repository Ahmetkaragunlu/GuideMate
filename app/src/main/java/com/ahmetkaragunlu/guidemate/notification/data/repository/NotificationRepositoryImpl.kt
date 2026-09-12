package com.ahmetkaragunlu.guidemate.notification.data.repository

import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.coroutines.ApplicationScope
import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.result.mapSuccess
import com.ahmetkaragunlu.guidemate.common.storage.installation.InstallationIdDataSource
import com.ahmetkaragunlu.guidemate.notification.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.notification.data.mapper.toDto
import com.ahmetkaragunlu.guidemate.notification.data.realtime.NotificationRealtimeClient
import com.ahmetkaragunlu.guidemate.notification.data.remote.api.NotificationApi
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.MarkRelatedNotificationsReadRequestDto
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.RegisterDeviceRequestDto
import com.ahmetkaragunlu.guidemate.notification.domain.device.PushInstallationIdProvider
import com.ahmetkaragunlu.guidemate.notification.domain.model.AppNotification
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferenceUpdate
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationPreferences
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetReference
import com.ahmetkaragunlu.guidemate.notification.domain.push.SystemNotificationController
import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
    private val realtimeClient: NotificationRealtimeClient,
    private val installationIdDataSource: InstallationIdDataSource,
    private val pushInstallationIdProvider: PushInstallationIdProvider,
    private val userRepository: UserRepository,
    private val apiCallExecutor: ApiCallExecutor,
    private val systemNotificationController: SystemNotificationController,
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

    private val notificationStateMutex = Mutex()
    private var currentPage = -1
    private var realtimeEventsJob: Job? = null
    @Volatile private var sessionGeneration = 0L

    init {
        observeAuthenticatedUser()
    }

    override suspend fun refreshNotifications(): DataResult<List<AppNotification>> {
        val session = currentSession()
        return notificationStateMutex.withLock {
            apiCallExecutor.execute(
                request = { api.getNotifications(page = 0, size = NOTIFICATION_PAGE_SIZE) },
                transform = { page -> page.content.map { it.toDomain() } to !page.isLast },
            ).mapSuccess { (notifications, hasMore) ->
                if (session.isCurrent()) {
                    currentPage = 0
                    mutableNotifications.value = notifications
                    mutableHasMoreNotifications.value = hasMore
                }
                notifications
            }
        }
    }

    override suspend fun loadMoreNotifications(): DataResult<List<AppNotification>> {
        val session = currentSession()
        return notificationStateMutex.withLock {
            if (!session.isCurrent()) return@withLock DataResult.Success(emptyList())
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
                if (session.isCurrent()) {
                    currentPage = nextPage
                    mutableNotifications.update { current -> mergeNotifications(current, incoming) }
                    mutableHasMoreNotifications.value = hasMore
                    mutableNotifications.value
                } else {
                    incoming
                }
            }
        }
    }

    override suspend fun refreshUnreadCount(): DataResult<Int> {
        val session = currentSession()
        return notificationStateMutex.withLock {
            apiCallExecutor.execute(
                request = api::getUnreadCount,
                transform = { it.unreadCount.toSafeInt() },
            ).mapSuccess { count ->
                if (session.isCurrent()) mutableUnreadCount.value = count
                count
            }
        }
    }

    override suspend fun markRead(notificationId: String): DataResult<AppNotification> {
        val session = currentSession()
        return notificationStateMutex.withLock {
            apiCallExecutor.execute(
                request = { api.markRead(notificationId) },
                transform = { it.toDomain() },
            ).mapSuccess { updated ->
                if (!session.isCurrent()) return@mapSuccess updated
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
                systemNotificationController.dismiss(updated.navigationTarget)
                updated
            }
        }
    }

    override suspend fun markAllRead(): DataResult<Int> {
        val session = currentSession()
        return notificationStateMutex.withLock {
            apiCallExecutor.execute(
                request = api::markAllRead,
                transform = { it.unreadCount.toSafeInt() },
            ).mapSuccess { unreadCount ->
                if (session.isCurrent()) {
                    mutableNotifications.update { notifications ->
                        notifications.map { it.copy(isRead = true) }
                    }
                    mutableUnreadCount.value = unreadCount
                    systemNotificationController.dismissAll()
                }
                unreadCount
            }
        }
    }

    override suspend fun markRelatedRead(
        target: NotificationTargetReference,
    ): DataResult<Int> {
        val session = currentSession()
        return notificationStateMutex.withLock {
            apiCallExecutor.execute(
                request = {
                    api.markRelatedRead(
                        MarkRelatedNotificationsReadRequestDto(
                            targetType = target.type.name,
                            targetId = target.targetId,
                        ),
                    )
                },
                transform = { it.unreadCount.toSafeInt() },
            ).mapSuccess { unreadCount ->
                if (session.isCurrent()) {
                    mutableNotifications.update { notifications ->
                        notifications.map { notification ->
                            if (!notification.isRead && target.matches(notification.payload)) {
                                notification.copy(isRead = true)
                            } else {
                                notification
                            }
                        }
                    }
                    mutableUnreadCount.value = unreadCount
                    systemNotificationController.dismissRelated(target)
                }
                unreadCount
            }
        }
    }

    override suspend fun refreshPreferences(): DataResult<NotificationPreferences> {
        val session = currentSession()
        return apiCallExecutor.execute(
            request = api::getPreferences,
            transform = { it.toDomain() },
        ).mapSuccess { preferences ->
            if (session.isCurrent()) mutablePreferences.value = preferences
            preferences
        }
    }

    override suspend fun updatePreferences(
        update: NotificationPreferenceUpdate,
    ): DataResult<NotificationPreferences> {
        val session = currentSession()
        return apiCallExecutor.execute(
            request = { api.updatePreferences(update.toDto()) },
            transform = { it.toDomain() },
        ).mapSuccess { preferences ->
            if (session.isCurrent()) mutablePreferences.value = preferences
            preferences
        }
    }

    override suspend fun registerDevice(pushInstallationId: String?): DataResult<Unit> {
        val session = currentSession()
        if (!userRepository.userState.value.isAuthenticated) return DataResult.Success(Unit)
        val installationId = installationIdDataSource.getOrCreate()
        val firebaseInstallationId =
            pushInstallationId ?: pushInstallationIdProvider.registerAndGetId()
        if (!session.isCurrent()) return DataResult.Success(Unit)
        return apiCallExecutor.executeUnit {
            api.registerDevice(
                RegisterDeviceRequestDto(
                    installationId = installationId,
                    firebaseInstallationId = firebaseInstallationId,
                ),
            )
        }
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

    override fun dismissSystemNotifications() {
        systemNotificationController.dismissAll()
    }

    override fun clearLocalState() {
        sessionGeneration++
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
                .collectLatest { userId ->
                    realtimeEventsJob?.cancel()
                    realtimeClient.disconnect()
                    clearLocalState()
                    if (userId != null) {
                        val session = currentSession()
                        observeRealtimeEvents(session)
                        realtimeClient.connect()
                        registerDevice()
                    }
                }
        }
    }

    private fun observeRealtimeEvents(session: SessionSnapshot) {
        realtimeEventsJob =
            applicationScope.launch {
                realtimeClient.events.collectLatest {
                    if (session.isCurrent()) {
                        refreshNotifications()
                        refreshUnreadCount()
                    }
                }
            }
    }

    private fun currentSession(): SessionSnapshot =
        SessionSnapshot(
            userId = userRepository.userState.value.userId,
            generation = sessionGeneration,
        )

    private fun SessionSnapshot.isCurrent(): Boolean =
        generation == sessionGeneration && userId == userRepository.userState.value.userId

    private data class SessionSnapshot(
        val userId: Long?,
        val generation: Long,
    )
}

private fun mergeNotifications(
    current: List<AppNotification>,
    incoming: List<AppNotification>,
): List<AppNotification> =
    (current + incoming)
        .distinctBy(AppNotification::notificationId)
        .sortedByDescending(AppNotification::createdAt)

private fun Long.toSafeInt(): Int = coerceIn(0, Int.MAX_VALUE.toLong()).toInt()
