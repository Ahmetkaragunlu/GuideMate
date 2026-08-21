package com.ahmetkaragunlu.guidemate.common.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R

@Composable
fun <T : GuideMateTab> GuideMateTabRow(
    tabs: List<T>,
    selectedTab: T,
    onTabSelected: (T) -> Unit,
) {
    val selectedIndex = tabs.indexOf(selectedTab)

    SecondaryTabRow(
        selectedTabIndex = selectedIndex,
        containerColor = Color.Transparent,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedIndex),
                height = 2.dp,
                color = colorResource(R.color.brand_color),
            )
        },
    ) {
        tabs.forEach { tab ->
            val isSelected = (tab == selectedTab)

            Tab(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = stringResource(id = tab.titleResId),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    )
                },
                selectedContentColor = colorResource(R.color.brand_color),
                unselectedContentColor = colorResource(R.color.text_color),
            )
        }
    }
}
