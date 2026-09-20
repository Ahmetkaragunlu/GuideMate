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
)

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
    @param:DrawableRes val coverImageResId: Int = R.drawable.ic_image_unavailable,
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
