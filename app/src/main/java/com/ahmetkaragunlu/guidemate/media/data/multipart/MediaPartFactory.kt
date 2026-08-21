package com.ahmetkaragunlu.guidemate.media.data.multipart

import com.ahmetkaragunlu.guidemate.common.result.AppError
import java.io.IOException
import okhttp3.MultipartBody

fun interface MediaPartFactory {
    fun create(localUri: String): MultipartBody.Part
}

internal class MediaPreparationException(
    val error: AppError,
    cause: Throwable? = null,
) : IOException(cause)
