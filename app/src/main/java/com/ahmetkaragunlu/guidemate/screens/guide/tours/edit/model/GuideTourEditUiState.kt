package com.ahmetkaragunlu.guidemate.screens.guide.tours.edit.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategory
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourLanguage
import java.time.LocalDate
import java.time.LocalTime

data class GuideTourEditUiState(
    val tourId: String = "",
    val sessionId: String = "",
    val title: String = "",
    val description: String = "",
    val country: String = "",
    val location: String = "",
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
    val selectedCoverImageUri: String? = null,
    val hasBookings: Boolean = false,
    val isTourIdentityLocked: Boolean = false,
    val approvalStatus: TourApprovalStatus? = null,
    val hasUnsavedChanges: Boolean = false,
    val requiresReviewConfirmation: Boolean = false,
    @param:StringRes val errorResId: Int? = null,
)
