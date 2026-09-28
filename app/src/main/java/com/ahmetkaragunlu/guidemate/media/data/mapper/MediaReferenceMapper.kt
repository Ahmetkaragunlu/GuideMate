package com.ahmetkaragunlu.guidemate.media.data.mapper

import com.ahmetkaragunlu.guidemate.media.data.remote.model.response.MediaReferenceResponse
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference

fun MediaReferenceResponse.toDomain(): MediaReference =
    MediaReference(
        mediaAssetId = mediaAssetId,
        imageUrl = imageUrl,
    )
