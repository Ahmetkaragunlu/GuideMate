package com.ahmetkaragunlu.guidemate.tour.presentation.tourist.detail

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourBookingAvailability

@get:StringRes
internal val TourBookingAvailability.detailMessageResId: Int?
    get() =
        when (this) {
            TourBookingAvailability.NOT_APPROVED -> R.string.tour_booking_not_approved
            TourBookingAvailability.CLOSED -> R.string.tour_booking_closed
            TourBookingAvailability.FULL -> R.string.tour_booking_full
            TourBookingAvailability.STARTED -> R.string.tour_booking_started
            TourBookingAvailability.UNAVAILABLE -> R.string.tour_booking_unavailable
            TourBookingAvailability.AVAILABLE,
            TourBookingAvailability.ALREADY_RESERVED,
            TourBookingAvailability.COMPLETED,
            TourBookingAvailability.CANCELLED,
            TourBookingAvailability.EXPIRED,
            -> null
        }
