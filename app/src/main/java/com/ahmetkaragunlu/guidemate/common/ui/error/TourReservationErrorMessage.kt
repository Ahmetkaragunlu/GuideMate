package com.ahmetkaragunlu.guidemate.common.ui.error

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider

internal fun BackendErrorCode.tourReservationErrorMessage(resourceProvider: ResourceProvider): String? =
    when (this) {
        BackendErrorCode.TOUR_NOT_FOUND -> resourceProvider.getString(R.string.error_tour_not_found)
        BackendErrorCode.TOUR_NOT_APPROVED -> resourceProvider.getString(R.string.error_tour_not_approved)
        BackendErrorCode.TOUR_CHANGE_PENDING -> resourceProvider.getString(R.string.error_tour_change_pending)
        BackendErrorCode.TOUR_LOCATION_LOCKED -> resourceProvider.getString(R.string.error_tour_location_locked)
        BackendErrorCode.TOUR_NOT_ARCHIVABLE -> resourceProvider.getString(R.string.error_tour_not_archivable)
        BackendErrorCode.TOUR_REVIEW_NOT_FOUND ->
            resourceProvider.getString(R.string.error_tour_review_not_found)
        BackendErrorCode.TOUR_REVIEW_STATE_INVALID ->
            resourceProvider.getString(R.string.error_tour_review_state_invalid)
        BackendErrorCode.INVALID_CATEGORY_CODE ->
            resourceProvider.getString(R.string.error_invalid_category_code)
        BackendErrorCode.INVALID_COUNTRY_CODE ->
            resourceProvider.getString(R.string.error_invalid_country_code)
        BackendErrorCode.INVALID_TIME_ZONE -> resourceProvider.getString(R.string.error_invalid_time_zone)
        BackendErrorCode.SESSION_NOT_FOUND -> resourceProvider.getString(R.string.error_session_not_found)
        BackendErrorCode.SESSION_NOT_BOOKABLE -> resourceProvider.getString(R.string.error_session_not_bookable)
        BackendErrorCode.SESSION_ALREADY_STARTED ->
            resourceProvider.getString(R.string.error_session_already_started)
        BackendErrorCode.SESSION_HAS_RESERVATIONS ->
            resourceProvider.getString(R.string.error_session_has_reservations)
        BackendErrorCode.CAPACITY_NOT_AVAILABLE ->
            resourceProvider.getString(R.string.error_capacity_not_available)
        BackendErrorCode.CAPACITY_BELOW_BOOKED_COUNT ->
            resourceProvider.getString(R.string.error_capacity_below_booked_count)
        BackendErrorCode.SESSION_STATUS_NOT_MANAGEABLE ->
            resourceProvider.getString(R.string.error_session_status_not_manageable)
        BackendErrorCode.SCHEDULE_CONFLICT -> resourceProvider.getString(R.string.error_schedule_conflict)
        BackendErrorCode.CONCURRENT_UPDATE -> resourceProvider.getString(R.string.error_concurrent_update)
        BackendErrorCode.RESERVATION_NOT_FOUND ->
            resourceProvider.getString(R.string.error_reservation_not_found)
        BackendErrorCode.RESERVATION_ALREADY_EXISTS ->
            resourceProvider.getString(R.string.error_reservation_already_exists)
        BackendErrorCode.RESERVATION_NOT_CANCELLABLE ->
            resourceProvider.getString(R.string.error_reservation_not_cancellable)
        BackendErrorCode.REVIEW_NOT_ALLOWED -> resourceProvider.getString(R.string.error_review_not_allowed)
        BackendErrorCode.REVIEW_ALREADY_EXISTS ->
            resourceProvider.getString(R.string.error_review_already_exists)
        else -> null
    }
