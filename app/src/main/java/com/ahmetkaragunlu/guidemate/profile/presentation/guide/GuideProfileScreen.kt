package com.ahmetkaragunlu.guidemate.profile.presentation.guide

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.image.GuideMateImage
import com.ahmetkaragunlu.guidemate.common.ui.image.ImageSourcePicker
import com.ahmetkaragunlu.guidemate.profile.presentation.level.GuideLevelInfoBottomSheet
import com.ahmetkaragunlu.guidemate.profile.presentation.level.model.GuideLevelViewerType
import com.ahmetkaragunlu.guidemate.profile.presentation.level.model.titleResId
import com.ahmetkaragunlu.guidemate.profile.presentation.components.CommonProfileMenuItem
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.components.ProfileStatsRow
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.model.GuideProfileMenuTarget
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.model.guideProfileMenuOptions

@Composable
fun GuideProfileScreen(
    viewModel: GuideProfileViewModel = hiltViewModel(),
    onNavigateToAccount: (GuideProfileMenuTarget) -> Unit = {},
    onNavigateToProfilePreview: () -> Unit = {},
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    var showGuideLevelInfoBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showPhotoSourceSheet by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val resources = LocalResources.current

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        val profileImageResId = profileState.profileImageResId
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier =
                    Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .clickable { showPhotoSourceSheet = true },
                contentAlignment = Alignment.Center,
            ) {
                if (profileImageResId != null) {
                    GuideMateImage(
                        fallbackImageResId = profileImageResId,
                        imageUrl = profileState.displayProfileImageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Icon(
                        Icons.Rounded.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(48.dp),
                    )
                }
            }

            Surface(
                onClick = { showPhotoSourceSheet = true },
                modifier =
                    Modifier
                        .size(32.dp)
                        .offset(x = 4.dp, y = 4.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.onPrimary,
                shadowElevation = 4.dp,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.PhotoCamera,
                        contentDescription = null,
                        tint = colorResource(R.color.brand_color),
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = profileState.displayName,
                color = colorResource(R.color.brand_color),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
            Text(
                text = profileState.title,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Surface(
            onClick = onNavigateToProfilePreview,
            shape = CircleShape,
            color = colorResource(R.color.brand_color).copy(alpha = 0.1f),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier.padding(
                        horizontal = dimensionResource(R.dimen.spacing_medium),
                        vertical = dimensionResource(R.dimen.spacing_tiny),
                    ),
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
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        ProfileStatsRow(
            guideLevel = stringResource(profileState.guideLevel.titleResId),
            rating = profileState.rating,
            tourCount = profileState.tourCount,
            onGuideLevelInfoClick = { showGuideLevelInfoBottomSheet = true },
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        Column(modifier = Modifier.fillMaxWidth()) {
            guideProfileMenuOptions.forEachIndexed { index, item ->
                CommonProfileMenuItem(
                    icon = item.icon,
                    title = stringResource(id = item.titleResId),
                    onClick = { onNavigateToAccount(item.target) },
                )

                if (index < guideProfileMenuOptions.lastIndex) {
                    HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                }
            }
        }
    }

    GuideLevelInfoBottomSheet(
        isVisible = showGuideLevelInfoBottomSheet,
        onDismiss = { showGuideLevelInfoBottomSheet = false },
        currentLevel = profileState.guideLevel,
        viewerType = GuideLevelViewerType.OWNER,
    )

    ImageSourcePicker(
        isVisible = showPhotoSourceSheet,
        titleResId = R.string.profile_photo_source_title,
        onDismissRequest = { showPhotoSourceSheet = false },
        onImageSelected = viewModel::onProfileImageSelected,
        onError = { errorResId ->
            Toast.makeText(context, resources.getString(errorResId), Toast.LENGTH_LONG).show()
        },
    )
}
