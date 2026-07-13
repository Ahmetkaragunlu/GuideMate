package com.ahmetkaragunlu.guidemate.components

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.BottomNavItem
import com.ahmetkaragunlu.guidemate.navigation.navigateBottomBar

@Composable
fun AppBottomBar(
    navController: NavController,
    currentRoute: String,
    items: List<BottomNavItem>,
    badgeCounts: Map<String, Int> = emptyMap(),
) {
    NavigationBar(containerColor = Color.Transparent) {
        items.forEach { item ->
            val badgeCount = badgeCounts[item.screen] ?: 0
            NavigationBarItem(
                label = { Text(stringResource(item.label)) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (badgeCount > 0) {
                                Badge {
                                    Text(text = if (badgeCount > 99) "99+" else badgeCount.toString())
                                }
                            }
                        },
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                        )
                    }
                },
                selected = currentRoute == item.screen,
                onClick = { navController.navigateBottomBar(item.screen) },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = colorResource(R.color.brand_color),
                        selectedTextColor = colorResource(R.color.brand_color),
                        indicatorColor = Color.Transparent,
                    ),
            )
        }
    }
}
