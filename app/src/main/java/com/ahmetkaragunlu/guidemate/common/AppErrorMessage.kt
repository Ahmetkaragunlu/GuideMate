package com.ahmetkaragunlu.guidemate.common

import com.ahmetkaragunlu.guidemate.R

fun AppError.toMessage(resourceProvider: ResourceProvider): String =
    when (this) {
        AppError.NoResponseFromServer -> resourceProvider.getString(R.string.error_no_response_from_server)
        AppError.GenericFailure -> resourceProvider.getString(R.string.error_generic_failure)
        AppError.SessionExpired -> resourceProvider.getString(R.string.error_session_expired)
        AppError.NoInternet -> resourceProvider.getString(R.string.error_no_internet)
        AppError.Unknown -> resourceProvider.getString(R.string.error_unknown)
        is AppError.Server -> resourceProvider.getString(R.string.error_server, code)
        is AppError.Backend -> message
    }
