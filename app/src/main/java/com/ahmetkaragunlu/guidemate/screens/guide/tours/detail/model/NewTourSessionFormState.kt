package com.ahmetkaragunlu.guidemate.screens.guide.tours.detail.model

import androidx.annotation.StringRes
import java.time.LocalDate
import java.time.LocalTime

data class NewTourSessionFormState(
    val timeZoneId: String = "",
    val selectedDate: LocalDate? = null,
    val selectedTime: LocalTime? = null,
    val durationMinutes: Int? = null,
    val meetingPoint: String = "",
    val price: String = "",
    val capacity: String = "",
    @param:StringRes val errorResId: Int? = null,
) {
    val canSubmit: Boolean
        get() =
            timeZoneId.isNotBlank() &&
                selectedDate != null &&
                selectedTime != null &&
                durationMinutes != null &&
                durationMinutes > 0 &&
                meetingPoint.isNotBlank() &&
                price.toDoubleOrNull()?.let { it > 0 } == true &&
                capacity.toIntOrNull()?.let { it > 0 } == true
}
