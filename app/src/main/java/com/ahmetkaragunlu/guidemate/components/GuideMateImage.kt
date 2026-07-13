package com.ahmetkaragunlu.guidemate.components

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.net.toUri
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val MAX_LOCAL_IMAGE_DECODE_SIZE = 2048

@Composable
fun GuideMateImage(
    @DrawableRes fallbackImageResId: Int,
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    colorFilter: ColorFilter? = null,
) {
    val context = LocalContext.current
    val bitmap by
        produceState<BitmapPainter?>(initialValue = null, key1 = imageUrl) {
            value =
                imageUrl
                    ?.takeIf { it.startsWith("content://") || it.startsWith("file://") }
                    ?.let { source ->
                        withContext(Dispatchers.IO) {
                            runCatching {
                                    context.contentResolver.decodeSampledBitmap(source.toUri())
                                        ?.let { bitmap -> BitmapPainter(bitmap.asImageBitmap()) }
                                }.getOrNull()
                        }
                    }
        }

    Image(
        painter = bitmap ?: painterResource(fallbackImageResId),
        contentDescription = contentDescription,
        contentScale = contentScale,
        colorFilter = colorFilter,
        modifier = modifier,
    )
}

private fun ContentResolver.decodeSampledBitmap(uri: Uri): Bitmap? {
    val source = ImageDecoder.createSource(this, uri)
    return ImageDecoder.decodeBitmap(source) { decoder, imageInfo, _ ->
        val width = imageInfo.size.width
        val height = imageInfo.size.height
        val largestDimension = maxOf(width, height)
        if (largestDimension > MAX_LOCAL_IMAGE_DECODE_SIZE) {
            val scale = MAX_LOCAL_IMAGE_DECODE_SIZE.toFloat() / largestDimension
            decoder.setTargetSize(
                (width * scale).roundToInt(),
                (height * scale).roundToInt(),
            )
        }
        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
    }
}
