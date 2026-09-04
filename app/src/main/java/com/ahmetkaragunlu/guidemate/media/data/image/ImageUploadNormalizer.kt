package com.ahmetkaragunlu.guidemate.media.data.image

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.core.net.toUri
import com.ahmetkaragunlu.guidemate.common.image.IMAGE_UPLOAD_JPEG_QUALITY
import com.ahmetkaragunlu.guidemate.common.image.MAX_IMAGE_UPLOAD_BYTES
import com.ahmetkaragunlu.guidemate.common.image.MAX_IMAGE_UPLOAD_DIMENSION_PX
import com.ahmetkaragunlu.guidemate.common.image.detectSupportedImageMimeType
import com.ahmetkaragunlu.guidemate.common.image.readImageSignature
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.media.data.multipart.MediaPreparationException
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import kotlin.math.max

private const val NORMALIZED_IMAGE_DIRECTORY = "normalized_uploads"

class ImageUploadNormalizer
@Inject
constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val contentResolver = context.contentResolver

    fun normalize(localUri: String): File {
        val uri = localUri.toSupportedUri()
        validateImageSignature(uri)

        var workingBitmap = decodeSampledBitmap(uri)
        try {
            workingBitmap = workingBitmap.replaceWith(workingBitmap.applyExifOrientation(readExifOrientation(uri)))
            workingBitmap = workingBitmap.replaceWith(workingBitmap.scaleToUploadBounds())
            workingBitmap = workingBitmap.replaceWith(workingBitmap.toOpaqueBitmap())
            return writeNormalizedImage(workingBitmap)
        } catch (exception: Exception) {
            if (!workingBitmap.isRecycled) workingBitmap.recycle()
            throw exception
        }
    }

    private fun String.toSupportedUri(): Uri {
        val uri = toUri()
        if (uri.scheme != ContentResolver.SCHEME_CONTENT && uri.scheme != ContentResolver.SCHEME_FILE) {
            throw MediaPreparationException(AppError.ImageUnavailable)
        }
        return uri
    }

    private fun validateImageSignature(uri: Uri) {
        val signature =
            readSource(uri) { input -> input.readImageSignature() }
                ?: throw MediaPreparationException(AppError.ImageUnavailable)
        if (detectSupportedImageMimeType(signature) == null) {
            throw MediaPreparationException(AppError.InvalidImageType)
        }
    }

    private fun decodeSampledBitmap(uri: Uri): Bitmap {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        readSource(uri) { input ->
                BitmapFactory.decodeStream(input, null, bounds)
                true
            }
            ?: throw MediaPreparationException(AppError.ImageUnavailable)
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) {
            throw MediaPreparationException(AppError.InvalidImageType)
        }

        val options =
            BitmapFactory.Options().apply {
                inSampleSize = calculateImageSampleSize(bounds.outWidth, bounds.outHeight)
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }
        return readSource(uri) { input -> BitmapFactory.decodeStream(input, null, options) }
            ?: throw MediaPreparationException(AppError.InvalidImageType)
    }

    private fun readExifOrientation(uri: Uri): Int =
        runCatching {
                readSource(uri) { input ->
                    ExifInterface(input)
                        .getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL,
                        )
                }
            }.getOrNull() ?: ExifInterface.ORIENTATION_NORMAL

    private inline fun <T> readSource(
        uri: Uri,
        block: (java.io.InputStream) -> T,
    ): T? =
        try {
            contentResolver.openInputStream(uri)?.use(block)
        } catch (exception: SecurityException) {
            throw MediaPreparationException(AppError.ImageUnavailable, exception)
        } catch (exception: IOException) {
            throw MediaPreparationException(AppError.ImageUnavailable, exception)
        }

    private fun writeNormalizedImage(bitmap: Bitmap): File {
        val outputDirectory = File(context.cacheDir, NORMALIZED_IMAGE_DIRECTORY).apply { mkdirs() }
        val outputFile =
            try {
                File.createTempFile("guidemate_upload_", ".jpg", outputDirectory)
            } catch (exception: IOException) {
                bitmap.recycle()
                throw MediaPreparationException(AppError.ImageUnavailable, exception)
            }
        var completed = false
        try {
            FileOutputStream(outputFile).buffered().use { output ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_UPLOAD_JPEG_QUALITY, output)) {
                    throw MediaPreparationException(AppError.ImageUnavailable)
                }
            }
            if (outputFile.length() <= 0L) {
                throw MediaPreparationException(AppError.ImageUnavailable)
            }
            if (outputFile.length() > MAX_IMAGE_UPLOAD_BYTES) {
                throw MediaPreparationException(AppError.ImageTooLarge)
            }
            completed = true
            return outputFile
        } catch (exception: MediaPreparationException) {
            throw exception
        } catch (exception: IOException) {
            throw MediaPreparationException(AppError.ImageUnavailable, exception)
        } finally {
            bitmap.recycle()
            if (!completed) outputFile.delete()
        }
    }
}

private fun Bitmap.replaceWith(replacement: Bitmap): Bitmap {
    if (replacement !== this) recycle()
    return replacement
}

internal fun calculateImageSampleSize(
    width: Int,
    height: Int,
): Int {
    val longestEdge = max(width, height)
    var sampleSize = 1
    while (longestEdge / (sampleSize * 2) >= MAX_IMAGE_UPLOAD_DIMENSION_PX) {
        sampleSize *= 2
    }
    return sampleSize
}

private fun Bitmap.applyExifOrientation(orientation: Int): Bitmap {
    val matrix = Matrix()
    when (orientation) {
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.setScale(1f, -1f)
        ExifInterface.ORIENTATION_TRANSPOSE -> {
            matrix.setRotate(90f)
            matrix.postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
        ExifInterface.ORIENTATION_TRANSVERSE -> {
            matrix.setRotate(-90f)
            matrix.postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
        else -> return this
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

private fun Bitmap.scaleToUploadBounds(): Bitmap {
    val longestEdge = max(width, height)
    if (longestEdge <= MAX_IMAGE_UPLOAD_DIMENSION_PX) return this

    val scale = MAX_IMAGE_UPLOAD_DIMENSION_PX.toFloat() / longestEdge
    return Bitmap.createScaledBitmap(
        this,
        (width * scale).toInt().coerceAtLeast(1),
        (height * scale).toInt().coerceAtLeast(1),
        true,
    )
}

private fun Bitmap.toOpaqueBitmap(): Bitmap {
    if (!hasAlpha()) return this

    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also { output ->
        Canvas(output).apply {
            drawColor(Color.WHITE)
            drawBitmap(this@toOpaqueBitmap, 0f, 0f, null)
        }
    }
}
