package com.ahmetkaragunlu.guidemate.tour.domain.usecase

import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.repository.MediaRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.TourReviewSubmission
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.CreateGuideTourInput
import com.ahmetkaragunlu.guidemate.tour.domain.repository.GuideTourRepository
import javax.inject.Inject
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class PublishGuideTourUseCase @Inject constructor(
    private val tourRepository: GuideTourRepository,
    private val mediaRepository: MediaRepository,
) {
    suspend operator fun invoke(
        coverImageUri: String,
        inputWithoutCover: CreateGuideTourInput,
    ): DataResult<TourReviewSubmission> {
        val upload = mediaRepository.uploadImage(coverImageUri, MediaPurpose.TOUR_COVER)
        if (upload is DataResult.Error) return upload

        val mediaAssetId = (upload as DataResult.Success).data.mediaAssetId
        val input =
            inputWithoutCover.copy(
                content = inputWithoutCover.content.copy(coverMediaId = mediaAssetId),
            )
        var isAttached = false
        return try {
            tourRepository.createTour(input).also { result ->
                isAttached = result is DataResult.Success
            }
        } finally {
            if (!isAttached) {
                withContext(NonCancellable) {
                    mediaRepository.deleteUnreferenced(mediaAssetId)
                }
            }
        }
    }
}
