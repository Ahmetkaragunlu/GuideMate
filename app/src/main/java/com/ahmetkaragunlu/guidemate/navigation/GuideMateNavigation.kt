package com.ahmetkaragunlu.guidemate.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.auth.authNavGraph
import com.ahmetkaragunlu.guidemate.navigation.guide.GuideNavigation
import com.ahmetkaragunlu.guidemate.navigation.guide.account.GuideAccountNavigation
import com.ahmetkaragunlu.guidemate.navigation.tourist.TouristNavigation
import com.ahmetkaragunlu.guidemate.navigation.tourist.account.TouristAccountNavigation
import com.ahmetkaragunlu.guidemate.auth.presentation.roleselection.RoleSelectionScreen

@Composable
fun GuideMateNavigation(
    viewModel: RootNavigationViewModel = hiltViewModel(),
) {
    LifecycleResumeEffect(Unit) {
        viewModel.setAppResumed(true)
        onPauseOrDispose { viewModel.setAppResumed(false) }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pendingNotificationTarget by
        viewModel.pendingNotificationTarget.collectAsStateWithLifecycle()
    if (!uiState.isReady) {
        RootNavigationLoading()
        return
    }

    val navController = rememberNavController()
    val rootBackStackEntry by navController.currentBackStackEntryAsState()
    val initialTarget = remember { uiState.target }
    var appliedTarget by remember { mutableStateOf(initialTarget) }

    LaunchedEffect(uiState.target) {
        if (uiState.target != appliedTarget) {
            navController.switchRoot(
                targetDestination = uiState.target.toRootDestination(),
                clearBackStackFrom = appliedTarget.toRootDestination(),
            )
            appliedTarget = uiState.target
        }
    }

    LaunchedEffect(pendingNotificationTarget, uiState.target, rootBackStackEntry?.destination) {
        if (pendingNotificationTarget == null) return@LaunchedEffect
        when {
            uiState.target == RootNavigationTarget.GUIDE &&
                rootBackStackEntry?.destination?.hasRoute<RootDestination.GuideAccount>() == true ->
                navController.popBackStack<RootDestination.Guide>(inclusive = false)
            uiState.target == RootNavigationTarget.TOURIST &&
                rootBackStackEntry?.destination?.hasRoute<RootDestination.TouristAccount>() == true ->
                navController.popBackStack<RootDestination.Tourist>(inclusive = false)
        }
    }

    NavHost(
        navController = navController,
        startDestination = initialTarget.toRootDestination(),
    ) {
        authNavGraph(
            navController = navController,
            startDestination = uiState.authStartDestination,
            onOnboardingCompleted = viewModel::completeOnboarding,
        )

        composable<RootDestination.RoleSelection> {
            RoleSelectionScreen()
        }

        composable<RootDestination.Tourist> {
            TouristNavigation(
                routeNavController = navController,
                onLogoutClick = viewModel::logout,
                pendingNotificationTarget = pendingNotificationTarget,
                onNotificationNavigationHandled = viewModel::onNotificationNavigationHandled,
                onVisibleNotificationTargetChanged = viewModel::setVisibleNotificationTarget,
            )
        }

        composable<RootDestination.Guide> {
            GuideNavigation(
                routeNavController = navController,
                onLogoutClick = viewModel::logout,
                pendingNotificationTarget = pendingNotificationTarget,
                onNotificationNavigationHandled = viewModel::onNotificationNavigationHandled,
                onVisibleNotificationTargetChanged = viewModel::setVisibleNotificationTarget,
            )
        }

        composable<RootDestination.GuideAccount> { backStackEntry ->
            val destination = backStackEntry.toRoute<RootDestination.GuideAccount>()
            GuideAccountNavigation(
                startDestination = destination.startDestination,
                onClose = navController::navigateUp,
            )
        }

        composable<RootDestination.TouristAccount> { backStackEntry ->
            val destination = backStackEntry.toRoute<RootDestination.TouristAccount>()
            TouristAccountNavigation(
                startDestination = destination.startDestination,
                onClose = navController::navigateUp,
            )
        }
    }
}

@Composable
private fun RootNavigationLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = colorResource(R.color.brand_color))
    }
}

private fun RootNavigationTarget.toRootDestination(): Any =
    when (this) {
        RootNavigationTarget.AUTH -> RootDestination.Auth
        RootNavigationTarget.ROLE_SELECTION -> RootDestination.RoleSelection
        RootNavigationTarget.GUIDE -> RootDestination.Guide
        RootNavigationTarget.TOURIST -> RootDestination.Tourist
    }
