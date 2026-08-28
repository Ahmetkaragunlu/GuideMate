package com.ahmetkaragunlu.guidemate.profile.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.image.GuideMateImage

@Composable
fun EditableProfileAvatar(
    imageUrl: String?,
    @DrawableRes fallbackImageResId: Int,
    isUpdating: Boolean,
    size: Dp,
    onClick: () -> Unit,
) {
    Box(contentAlignment = Alignment.BottomEnd) {
        Box(
            modifier =
                Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable(enabled = !isUpdating, onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            GuideMateImage(
                fallbackImageResId = fallbackImageResId,
                imageUrl = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
        }

        Surface(
            onClick = onClick,
            enabled = !isUpdating,
            modifier = Modifier.size(32.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.onPrimary,
            shadowElevation = 4.dp,
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isUpdating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = colorResource(R.color.brand_color),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.PhotoCamera,
                        contentDescription = null,
                        tint = colorResource(R.color.brand_color),
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}
