package com.ahmetkaragunlu.guidemate.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import com.ahmetkaragunlu.guidemate.auth.domain.repository.AuthRepository
import com.ahmetkaragunlu.guidemate.auth.domain.repository.OnboardingRepository
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.navigation.auth.AuthStartDestination
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.navigation.NotificationNavigationCoordinator
import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class RootNavigationTarget {
    AUTH,
    ROLE_SELECTION,
    GUIDE,
    TOURIST,
}

data class RootNavigationUiState(
    val isReady: Boolean = false,
    val target: RootNavigationTarget = RootNavigationTarget.AUTH,
    val authStartDestination: AuthStartDestination = AuthStartDestination.SIGN_IN,
)

@HiltViewModel
class RootNavigationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val onboardingRepository: OnboardingRepository,
    private val notificationRepository: NotificationRepository,
    private val notificationNavigationCoordinator: NotificationNavigationCoordinator,
) : ViewModel() {
    private var authStartDestination = AuthStartDestination.SIGN_IN
    private val _uiState = MutableStateFlow(RootNavigationUiState())
    val uiState: StateFlow<RootNavigationUiState> = _uiState.asStateFlow()
    val pendingNotificationTarget: StateFlow<NotificationNavigationTarget?> =
        notificationNavigationCoordinator.pendingTarget

    init {
        viewModelScope.launch {
            restoreSession()
            userRepository.userState.collectLatest(::publishUserState)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            notificationRepository.clearLocalState()
        }
    }

    fun onNotificationNavigationHandled(target: NotificationNavigationTarget) {
        notificationNavigationCoordinator.consume(target)
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            onboardingRepository.complete()
            authStartDestination = AuthStartDestination.SIGN_IN
            _uiState.value =
                _uiState.value.copy(
                    authStartDestination = AuthStartDestination.SIGN_IN,
                )
        }
    }

    private suspend fun restoreSession() {
        val onboardingCompleted = onboardingRepository.isCompleted()
        if (!authRepository.hasStoredSession()) {
            authStartDestination =
                if (onboardingCompleted) {
                    AuthStartDestination.SIGN_IN
                } else {
                    AuthStartDestination.ONBOARDING
                }
            authRepository.clearLocalSession()
            publishUserState(UserState())
            return
        }

        authStartDestination = AuthStartDestination.SIGN_IN
        if (!onboardingCompleted) {
            onboardingRepository.complete()
        }

        val cachedUser = userRepository.restoreCachedUser()
        if (cachedUser.isAuthenticated) {
            publishUserState(cachedUser)
        }

        when (val result = authRepository.currentUser()) {
            is DataResult.Success -> publishUserState(result.data)
            is DataResult.Error -> {
                if (!authRepository.hasStoredSession() || !cachedUser.isAuthenticated) {
                    publishUserState(UserState())
                }
            }
        }
    }

    private fun publishUserState(userState: UserState) {
        _uiState.value =
            RootNavigationUiState(
                isReady = true,
                target = userState.toNavigationTarget(),
                authStartDestination = authStartDestination,
            )
    }

    private fun UserState.toNavigationTarget(): RootNavigationTarget =
        when {
            !isAuthenticated -> RootNavigationTarget.AUTH
            !isRoleSelected || role == null -> RootNavigationTarget.ROLE_SELECTION
            role == UserRole.GUIDE -> RootNavigationTarget.GUIDE
            else -> RootNavigationTarget.TOURIST
        }
}
