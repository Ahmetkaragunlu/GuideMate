package com.ahmetkaragunlu.guidemate.tour.domain.usecase

import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.repository.MediaRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.TourReviewSubmission
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.SubmitTourChangeInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourContentInput
import com.ahmetkaragunlu.guidemate.tour.domain.repository.GuideTourRepository
import javax.inject.Inject
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class SubmitGuideTourContentChangeUseCase @Inject constructor(
    private val tourRepository: GuideTourRepository,
    private val mediaRepository: MediaRepository,
) {
    suspend operator fun invoke(
        tourId: String,
        baseVersion: Long,
        content: TourContentInput,
        newCoverImageUri: String?,
    ): DataResult<TourReviewSubmission> {
        val upload =
            newCoverImageUri?.let { uri ->
                when (val result = mediaRepository.uploadImage(uri, MediaPurpose.TOUR_COVER)) {
                    is DataResult.Error -> return result
                    is DataResult.Success -> result.data
                }
            }
        val submittedContent =
            upload?.let { content.copy(coverMediaId = it.mediaAssetId) } ?: content
        var isAttached = upload == null
        return try {
            tourRepository
                .submitChange(
                    tourId = tourId,
                    input =
                        SubmitTourChangeInput(
                            baseVersion = baseVersion,
                            content = submittedContent,
                        ),
                ).also { result ->
                    isAttached = result is DataResult.Success
                }
        } finally {
            if (!isAttached && upload != null) {
                withContext(NonCancellable) {
                    mediaRepository.deleteUnreferenced(upload.mediaAssetId)
                }
            }
        }
    }
}
