package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import java.time.LocalDate
import java.time.LocalTime
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab

data class GuideTourEditUiState(
    val tourId: String = "",
    val sessionId: String = "",
    val tourVersion: Long = 0,
    val sessionVersion: Long = 0,
    val title: String = "",
    val description: String = "",
    val country: String = "",
    val countryCode: String = "",
    val location: String = "",
    val cityPlaceId: String = "",
    val timeZoneId: String = "",
    val category: TourCategory? = null,
    val meetingPoint: String = "",
    val tourDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val durationMinutes: String = "",
    val price: String = "",
    val capacity: String = "",
    val languages: List<TourLanguage> = emptyList(),
    @param:DrawableRes val coverImageResId: Int = R.drawable.example,
    val coverImageUrl: String? = null,
    val coverMediaId: String? = null,
    val selectedCoverImageUri: String? = null,
    val hasBookings: Boolean = false,
    val isTourIdentityLocked: Boolean = false,
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
