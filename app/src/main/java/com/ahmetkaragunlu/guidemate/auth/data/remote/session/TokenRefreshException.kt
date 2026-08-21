package com.ahmetkaragunlu.guidemate.auth.data.remote.session

import com.ahmetkaragunlu.guidemate.common.result.AppError
import java.io.IOException

class TokenRefreshException(
    val error: AppError,
    cause: Throwable? = null,
) : IOException(cause)
