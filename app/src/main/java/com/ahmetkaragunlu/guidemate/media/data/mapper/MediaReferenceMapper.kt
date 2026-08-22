package com.ahmetkaragunlu.guidemate.media.data.mapper

import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaReferenceResponseDto
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference

fun MediaReferenceResponseDto.toDomain(): MediaReference =
    MediaReference(
        mediaAssetId = mediaAssetId,
        imageUrl = imageUrl,
    )
