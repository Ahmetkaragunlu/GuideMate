package com.ahmetkaragunlu.guidemate.auth.data.remote.session

import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkErrorCarrier
import java.io.IOException

class TokenRefreshException(
    override val error: AppError,
    cause: Throwable? = null,
) : IOException(cause), NetworkErrorCarrier
