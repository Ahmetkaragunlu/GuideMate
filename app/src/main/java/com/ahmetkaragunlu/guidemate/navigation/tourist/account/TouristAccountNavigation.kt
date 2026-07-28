package com.ahmetkaragunlu.guidemate.navigation.tourist.account

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TouristAccountNavigation(
    startDestination: TouristAccountStart,
    onClose: () -> Unit,
) {
    val accountNavController = rememberNavController()
    val navBackStackEntry by accountNavController.currentBackStackEntryAsState()
    val titleResId = navBackStackEntry?.destination.touristAccountTitleResId()

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
                    IconButton(
                        onClick = {
                            if (!accountNavController.navigateUp()) {
                                onClose()
                            }
                        },
                    ) {
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
            startDestination = startDestination.toDestination(),
            modifier = Modifier.padding(innerPadding),
        ) {
            touristAccountNavGraph(accountNavController)
        }
    }
}
