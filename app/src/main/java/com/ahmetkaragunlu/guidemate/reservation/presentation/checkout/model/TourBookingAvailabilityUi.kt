package com.ahmetkaragunlu.guidemate.reservation.presentation.checkout.model

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourBookingAvailability

@get:StringRes
val TourBookingAvailability.detailMessageResId: Int?
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

@get:StringRes
val TourBookingAvailability.checkoutErrorResId: Int
    get() =
        when (this) {
            TourBookingAvailability.ALREADY_RESERVED -> R.string.checkout_error_already_reserved
            TourBookingAvailability.NOT_APPROVED -> R.string.tour_booking_not_approved
            TourBookingAvailability.CLOSED -> R.string.tour_booking_closed
            TourBookingAvailability.FULL -> R.string.tour_booking_full
            TourBookingAvailability.STARTED -> R.string.tour_booking_started
            TourBookingAvailability.COMPLETED -> R.string.checkout_error_tour_completed
            TourBookingAvailability.CANCELLED -> R.string.checkout_error_tour_cancelled
            TourBookingAvailability.EXPIRED -> R.string.checkout_error_tour_expired
            TourBookingAvailability.UNAVAILABLE -> R.string.tour_booking_unavailable
            TourBookingAvailability.AVAILABLE -> error("Available tours do not have a checkout error")
        }
