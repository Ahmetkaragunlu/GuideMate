package com.ahmetkaragunlu.guidemate.screens.guide.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.profile.components.ProfileMenuItem
import com.ahmetkaragunlu.guidemate.screens.guide.profile.components.ProfileStatsRow
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.guideProfileMenuItems

@Composable
fun GuideProfileScreen(
    viewModel: GuideProfileViewModel = hiltViewModel()
) {
    val firstName by viewModel.firstName.collectAsStateWithLifecycle()
    val lastName by viewModel.lastName.collectAsStateWithLifecycle()
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

    val displayName = "${firstName ?: "Ahmet"} ${lastName ?: "Karagünlü"}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }

            Surface(
                onClick = { },
                modifier = Modifier
                    .size(32.dp)
                    .offset(x = 4.dp, y = 4.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.onPrimary,
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.PhotoCamera,
                        contentDescription = null,
                        tint = colorResource(R.color.brand_color),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = displayName,
                color = colorResource(R.color.brand_color),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
            Text(
                text = profileState.title,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Surface(
            onClick = { },
            shape = CircleShape,
            color = colorResource(R.color.brand_color).copy(alpha = 0.1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.spacing_medium),
                    vertical = dimensionResource(R.dimen.spacing_tiny)
                )
            ) {
                Text(
                    text = "👀",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.view_as_tourist),
                    color = colorResource(R.color.brand_color),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        ProfileStatsRow(
            guideLevel = profileState.guideLevel,
            rating = profileState.rating,
            tourCount = profileState.tourCount,
            onGuideLevelInfoClick = { /* TODO: BottomSheet Göster */ }
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        Column(modifier = Modifier.fillMaxWidth()) {
            guideProfileMenuItems.forEachIndexed { index, item ->
                ProfileMenuItem(
                    icon = item.icon,
                    title = stringResource(id = item.titleResId),
                    onClick = item.onClick
                )

                if (index < guideProfileMenuItems.lastIndex) {
                    HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                }
            }
        }
    }
}