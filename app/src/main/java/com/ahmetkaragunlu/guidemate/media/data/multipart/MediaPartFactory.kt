package com.ahmetkaragunlu.guidemate.media.data.multipart

import com.ahmetkaragunlu.guidemate.common.result.AppError
import java.io.Closeable
import java.io.IOException
import okhttp3.MultipartBody

fun interface MediaPartFactory {
    suspend fun create(localUri: String): PreparedMediaPart
}

class PreparedMediaPart(
    val part: MultipartBody.Part,
    private val onClose: () -> Unit = {},
) : Closeable {
    override fun close() = onClose()
}

internal class MediaPreparationException(
    val error: AppError,
    cause: Throwable? = null,
) : IOException(cause)
