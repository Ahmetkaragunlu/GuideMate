package com.ahmetkaragunlu.guidemate.common.ui.image

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest

@Composable
fun GuideMateImage(
    @DrawableRes fallbackImageResId: Int,
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    colorFilter: ColorFilter? = null,
) {
    val fallbackPainter = painterResource(fallbackImageResId)
    val model =
        ImageRequest
            .Builder(LocalContext.current)
            .data(imageUrl?.takeIf(String::isNotBlank))
            .build()

    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        placeholder = fallbackPainter,
        error = fallbackPainter,
        fallback = fallbackPainter,
        contentScale = contentScale,
        colorFilter = colorFilter,
        modifier = modifier,
    )
}
