package com.ahmetkaragunlu.guidemate.data.remote.error

import com.ahmetkaragunlu.guidemate.common.AppError
import java.io.IOException

class TokenRefreshException(
    val error: AppError,
    cause: Throwable? = null,
) : IOException(cause)
