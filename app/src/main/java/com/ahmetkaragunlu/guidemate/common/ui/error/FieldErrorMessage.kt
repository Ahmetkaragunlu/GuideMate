package com.ahmetkaragunlu.guidemate.common.ui.error

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.AppFieldError
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider

internal fun AppFieldError.toFieldMessage(resourceProvider: ResourceProvider): String =
    when (code) {
        "FIELD_REQUIRED" -> resourceProvider.getString(R.string.error_field_required)
        "INVALID_EMAIL" -> resourceProvider.getString(R.string.email_error_message)
        "INVALID_SIZE" -> resourceProvider.getString(R.string.error_invalid_field_size)
        "INVALID_FORMAT" -> resourceProvider.getString(R.string.error_invalid_field_format)
        else -> resourceProvider.getString(R.string.error_invalid_field)
    }
