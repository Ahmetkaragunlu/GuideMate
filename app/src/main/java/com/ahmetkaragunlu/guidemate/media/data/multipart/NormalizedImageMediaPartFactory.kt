package com.ahmetkaragunlu.guidemate.media.data.multipart

import com.ahmetkaragunlu.guidemate.media.data.image.ImageUploadNormalizer
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

private const val PART_NAME = "file"
private const val NORMALIZED_IMAGE_CONTENT_TYPE = "image/jpeg"

class NormalizedImageMediaPartFactory
@Inject
constructor(
    private val imageNormalizer: ImageUploadNormalizer,
) : MediaPartFactory {
    override suspend fun create(localUri: String): PreparedMediaPart =
        withContext(Dispatchers.IO) {
            val normalizedFile = imageNormalizer.normalize(localUri)
            val requestBody = normalizedFile.asRequestBody(NORMALIZED_IMAGE_CONTENT_TYPE.toMediaType())
            val part =
                MultipartBody.Part.createFormData(
                    PART_NAME,
                    normalizedFile.name,
                    requestBody,
                )
            PreparedMediaPart(part = part, onClose = normalizedFile::delete)
        }
}
