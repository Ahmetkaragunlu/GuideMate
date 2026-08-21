package com.ahmetkaragunlu.guidemate.media.data.multipart

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.OpenableColumns
import android.net.Uri
import androidx.core.net.toUri
import com.ahmetkaragunlu.guidemate.common.image.MAX_IMAGE_UPLOAD_BYTES
import com.ahmetkaragunlu.guidemate.common.image.detectSupportedImageMimeType
import com.ahmetkaragunlu.guidemate.common.image.readImageSignature
import com.ahmetkaragunlu.guidemate.common.result.AppError
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source

class ContentResolverMediaPartFactory @Inject constructor(
    @ApplicationContext context: Context,
) : MediaPartFactory {
    private val contentResolver = context.contentResolver

    override fun create(localUri: String): MultipartBody.Part {
        val uri = localUri.toSupportedUri()
        val sizeBytes = contentResolver.readSize(uri)
        if (sizeBytes == 0L) throw MediaPreparationException(AppError.ImageUnavailable)
        if (sizeBytes > MAX_IMAGE_UPLOAD_BYTES) {
            throw MediaPreparationException(AppError.ImageTooLarge)
        }

        val contentType =
            contentResolver.readSignature(uri).let(::detectSupportedImageMimeType)
                ?: throw MediaPreparationException(AppError.InvalidImageType)
        val fileName = contentResolver.readSafeFileName(uri, contentType)
        val requestBody = ContentUriRequestBody(contentResolver, uri, contentType, sizeBytes)
        return MultipartBody.Part.createFormData(PART_NAME, fileName, requestBody)
    }

    private fun String.toSupportedUri(): Uri {
        val uri = toUri()
        if (uri.scheme != ContentResolver.SCHEME_CONTENT && uri.scheme != ContentResolver.SCHEME_FILE) {
            throw MediaPreparationException(AppError.ImageUnavailable)
        }
        return uri
    }

    private fun ContentResolver.readSize(uri: Uri): Long =
        runCatching {
                openAssetFileDescriptor(uri, "r")?.use { descriptor -> descriptor.length }
            }.getOrNull()
            ?: UNKNOWN_CONTENT_LENGTH

    private fun ContentResolver.readSignature(uri: Uri): ByteArray =
        try {
            openInputStream(uri)?.use { input -> input.readImageSignature() }
                ?: throw MediaPreparationException(AppError.ImageUnavailable)
        } catch (exception: SecurityException) {
            throw MediaPreparationException(AppError.ImageUnavailable, exception)
        } catch (exception: IOException) {
            throw MediaPreparationException(AppError.ImageUnavailable, exception)
        }

    private fun ContentResolver.readSafeFileName(
        uri: Uri,
        contentType: String,
    ): String {
        val displayName =
            runCatching {
                    query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use {
                        cursor -> cursor.readDisplayName()
                    }
                }.getOrNull()
        val extension = extensionFor(contentType)
        val sanitized =
            displayName
                ?.substringAfterLast('/')
                ?.substringAfterLast('\\')
                ?.substringBeforeLast('.', missingDelimiterValue = displayName)
                ?.replace(UNSAFE_FILE_NAME_CHARACTERS, "_")
                ?.take(MAX_FILE_NAME_STEM_LENGTH)
                ?.takeIf(String::isNotBlank)
        return "${sanitized ?: DEFAULT_FILE_NAME}.$extension"
    }

    private fun Cursor.readDisplayName(): String? =
        if (moveToFirst()) {
            getColumnIndex(OpenableColumns.DISPLAY_NAME)
                .takeIf { it >= 0 }
                ?.let(::getString)
        } else {
            null
        }

    private fun extensionFor(contentType: String): String =
        when (contentType) {
            "image/jpeg" -> "jpg"
            "image/png" -> "png"
            else -> "webp"
        }

    private companion object {
        const val PART_NAME = "file"
        const val DEFAULT_FILE_NAME = "guidemate-image"
        const val UNKNOWN_CONTENT_LENGTH = -1L
        const val MAX_FILE_NAME_STEM_LENGTH = 110
        val UNSAFE_FILE_NAME_CHARACTERS = Regex("[^A-Za-z0-9._-]")
    }
}

private class ContentUriRequestBody(
    private val contentResolver: ContentResolver,
    private val uri: Uri,
    contentType: String,
    private val sizeBytes: Long,
) : RequestBody() {
    private val mediaType: MediaType = contentType.toMediaType()

    override fun contentType(): MediaType = mediaType

    override fun contentLength(): Long = sizeBytes

    override fun writeTo(sink: BufferedSink) {
        val input = contentResolver.openInputStream(uri) ?: throw IOException("Image cannot be read")
        input.source().use(sink::writeAll)
    }
}
