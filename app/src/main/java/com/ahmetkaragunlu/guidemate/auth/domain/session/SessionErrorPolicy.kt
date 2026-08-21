package com.ahmetkaragunlu.guidemate.auth.domain.session

import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode

fun AppError.isTerminalSessionError(): Boolean =
    this is AppError.SessionExpired ||
        (
            this is AppError.Backend &&
                code in
                    setOf(
                        BackendErrorCode.ACCOUNT_DISABLED,
                        BackendErrorCode.INVALID_REFRESH_TOKEN,
                        BackendErrorCode.REFRESH_TOKEN_EXPIRED,
                        BackendErrorCode.REFRESH_TOKEN_REPLAY,
                        BackendErrorCode.UNAUTHORIZED,
                    )
        )
