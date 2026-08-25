package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab
import java.time.LocalDate
import java.time.LocalTime

data class GuideTourEditUiState(
    val identity: GuideTourEditIdentityState = GuideTourEditIdentityState(),
    val content: GuideTourEditContentFormState = GuideTourEditContentFormState(),
    val session: GuideTourEditSessionFormState = GuideTourEditSessionFormState(),
    val operation: GuideTourEditOperationState = GuideTourEditOperationState(),
) {
    val tourId get() = identity.tourId
    val sessionId get() = identity.sessionId
    val tourVersion get() = identity.tourVersion
    val sessionVersion get() = identity.sessionVersion
    val country get() = identity.country
    val countryCode get() = identity.countryCode
    val location get() = identity.location
    val cityPlaceId get() = identity.cityPlaceId
    val timeZoneId get() = identity.timeZoneId
    val isTourIdentityLocked get() = identity.isTourIdentityLocked
    val title get() = content.title
    val description get() = content.description
    val category get() = content.category
    val languages get() = content.languages
    val coverImageResId get() = content.coverImageResId
    val coverImageUrl get() = content.coverImageUrl
    val coverMediaId get() = content.coverMediaId
    val selectedCoverImageUri get() = content.selectedCoverImageUri
    val meetingPoint get() = session.meetingPoint
    val tourDate get() = session.tourDate
    val startTime get() = session.startTime
    val durationMinutes get() = session.durationMinutes
    val price get() = session.price
    val capacity get() = session.capacity
    val hasBookings get() = session.hasBookings
    val approvalStatus get() = operation.approvalStatus
    val hasUnsavedChanges get() = operation.hasUnsavedChanges
    val requiresReviewConfirmation get() = operation.requiresReviewConfirmation
    val contentReviewSubmitted get() = operation.contentReviewSubmitted
    val loadState get() = operation.loadState
    val isSaving get() = operation.isSaving
    val userMessage get() = operation.userMessage
    val savedTargetTab get() = operation.savedTargetTab
    val errorResId get() = operation.errorResId
}

data class GuideTourEditIdentityState(
    val tourId: String = "",
    val sessionId: String = "",
    val tourVersion: Long = 0,
    val sessionVersion: Long = 0,
    val country: String = "",
    val countryCode: String = "",
    val location: String = "",
    val cityPlaceId: String = "",
    val timeZoneId: String = "",
    val isTourIdentityLocked: Boolean = false,
)

data class GuideTourEditContentFormState(
    val title: String = "",
    val description: String = "",
    val category: TourCategory? = null,
    val languages: List<TourLanguage> = emptyList(),
    @param:DrawableRes val coverImageResId: Int = R.drawable.example,
    val coverImageUrl: String? = null,
    val coverMediaId: String? = null,
    val selectedCoverImageUri: String? = null,
)

data class GuideTourEditSessionFormState(
    val meetingPoint: String = "",
    val tourDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val durationMinutes: String = "",
    val price: String = "",
    val capacity: String = "",
    val hasBookings: Boolean = false,
)

data class GuideTourEditOperationState(
    val approvalStatus: TourApprovalStatus? = null,
    val hasUnsavedChanges: Boolean = false,
    val requiresReviewConfirmation: Boolean = false,
    val contentReviewSubmitted: Boolean = false,
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val isSaving: Boolean = false,
    val userMessage: String? = null,
    val savedTargetTab: GuideTourTab? = null,
    @param:StringRes val errorResId: Int? = null,
)
