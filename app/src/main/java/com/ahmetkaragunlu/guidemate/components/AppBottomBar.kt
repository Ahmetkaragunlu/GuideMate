package com.ahmetkaragunlu.guidemate.components

import androidx.annotation.StringRes
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.ahmetkaragunlu.guidemate.R

data class BottomBarItem<T>(
    @param:StringRes val label: Int,
    val icon: ImageVector,
    val destination: T,
)

@Composable
fun <T> AppBottomBar(
    selectedDestination: T?,
    items: List<BottomBarItem<T>>,
    badgeCounts: Map<T, Int> = emptyMap(),
    onDestinationClick: (T) -> Unit,
) {
    NavigationBar(containerColor = Color.Transparent) {
        items.forEach { item ->
            val badgeCount = badgeCounts[item.destination] ?: 0
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
                selected = selectedDestination == item.destination,
                onClick = { onDestinationClick(item.destination) },
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
