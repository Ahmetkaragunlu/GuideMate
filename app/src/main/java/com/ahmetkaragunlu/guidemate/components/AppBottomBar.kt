package com.ahmetkaragunlu.guidemate.components


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.ahmetkaragunlu.guidemate.R

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ahmetkaragunlu.guidemate.navigation.BottomNavItem
import com.ahmetkaragunlu.guidemate.navigation.navigateBottomBar

@Composable
fun AppBottomBar(
    navController: NavController,
    currentRoute: String,
    items: List<BottomNavItem>
) {


    NavigationBar(containerColor = Color.Transparent) {
        items.forEach { item ->
            NavigationBarItem(
                label = { Text(stringResource(item.label)) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                    )
                },
                selected = currentRoute == item.screen,
                onClick = { navController.navigateBottomBar(item.screen) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(R.color.brand_color),
                    selectedTextColor = colorResource(R.color.brand_color),
                    indicatorColor = Color.Transparent
               )
            )
        }
    }
}