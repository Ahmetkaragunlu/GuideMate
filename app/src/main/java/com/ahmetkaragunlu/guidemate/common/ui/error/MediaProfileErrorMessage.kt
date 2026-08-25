package com.ahmetkaragunlu.guidemate.common.ui.error

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider

internal fun BackendErrorCode.mediaProfileErrorMessage(resourceProvider: ResourceProvider): String? =
    when (this) {
        BackendErrorCode.MEDIA_NOT_FOUND -> resourceProvider.getString(R.string.error_media_not_found)
        BackendErrorCode.MEDIA_INVALID_TYPE -> resourceProvider.getString(R.string.error_image_invalid_type)
        BackendErrorCode.MEDIA_TOO_LARGE -> resourceProvider.getString(R.string.error_image_too_large)
        BackendErrorCode.MEDIA_STORAGE_FAILED ->
            resourceProvider.getString(R.string.error_media_storage_failed)
        BackendErrorCode.MEDIA_IN_USE -> resourceProvider.getString(R.string.error_media_in_use)
        BackendErrorCode.MEDIA_PURPOSE_MISMATCH ->
            resourceProvider.getString(R.string.error_media_purpose_mismatch)
        BackendErrorCode.GUIDE_PROFILE_NOT_FOUND ->
            resourceProvider.getString(R.string.guide_profile_not_found)
        BackendErrorCode.INVALID_LANGUAGE_CODE ->
            resourceProvider.getString(R.string.error_invalid_language_code)
        else -> null
    }
