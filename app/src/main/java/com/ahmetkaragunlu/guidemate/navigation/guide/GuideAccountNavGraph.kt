package com.ahmetkaragunlu.guidemate.navigation.guide

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.AboutLanguagesScreen
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.AccountSettingsScreen
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.BankAccountsScreen
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.HelpFaqScreen
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft

fun NavGraphBuilder.guideAccountNavGraph(accountNavController: NavController) {
    composable(route = GuideAccountRoute.BankAccounts.route) {
        BankAccountsScreen()
    }
    composable(route = GuideAccountRoute.AboutLanguages.route) {
        AboutLanguagesScreen()
    }
    composable(route = GuideAccountRoute.AccountSettings.route) {
        AccountSettingsScreen()
    }
    composable(route = GuideAccountRoute.HelpFaq.route) {
        HelpFaqScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideAccountNavGraphScaffold(
    routeNavController: NavController,
    startDestination: String = GuideAccountRoute.BankAccounts.route,
) {
    val accountNavController = rememberNavController()
    val titleResId = GuideAccountRoute.fromRoute(startDestination)?.titleResId ?: R.string.menu_account_settings

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = titleResId),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { routeNavController.navigateUp() }) {
                        Icon(
                            imageVector = TablerIcons.ArrowLeft,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = accountNavController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
        ) {
            guideAccountNavGraph(accountNavController = accountNavController)
        }
    }
}
