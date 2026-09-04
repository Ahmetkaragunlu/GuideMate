package com.ahmetkaragunlu.guidemate.navigation.notification

import com.ahmetkaragunlu.guidemate.navigation.chat.ChatDestination
import com.ahmetkaragunlu.guidemate.navigation.guide.GuideDestination
import com.ahmetkaragunlu.guidemate.navigation.guide.tours.GuideTourDestination
import com.ahmetkaragunlu.guidemate.navigation.guide.wallet.GuideWalletDestination
import com.ahmetkaragunlu.guidemate.navigation.tourist.TouristDestination
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.TouristPaymentDestination
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType

internal fun NotificationNavigationTarget.toGuideDestination(): Any =
    when (type) {
        NotificationType.CHAT_MESSAGE ->
            chatId?.let(ChatDestination::Detail) ?: GuideDestination.Home
        NotificationType.TOUR_APPROVED,
        NotificationType.TOUR_REJECTED,
        NotificationType.TOUR_CHANGE_APPROVED,
        NotificationType.TOUR_CHANGE_REJECTED,
        NotificationType.TOUR_PURCHASED,
        NotificationType.RESERVATION_CANCELLED,
        NotificationType.TOUR_CANCELLED,
        NotificationType.TOUR_COMPLETED,
        NotificationType.RATING_RECEIVED,
        NotificationType.COMMENT_RECEIVED,
        NotificationType.UPCOMING_TOUR_REMINDER,
        -> guideTourDestinationOrFallback()
        NotificationType.PAYMENT_SUCCEEDED,
        NotificationType.PAYMENT_FAILED,
        NotificationType.REFUND_REQUESTED,
        NotificationType.REFUND_COMPLETED,
        NotificationType.REFUND_FAILED,
        NotificationType.REFUND_MANUAL_REVIEW,
        NotificationType.EARNING_AVAILABLE,
        NotificationType.WITHDRAWAL_COMPLETED,
        -> GuideWalletDestination.WalletTransactions
        NotificationType.SECURITY_ALERT -> GuideDestination.Profile
        NotificationType.RESERVATION_CONFIRMED,
        NotificationType.REVIEW_REQUEST,
        NotificationType.UNKNOWN,
        -> GuideDestination.Home
    }

internal fun NotificationNavigationTarget.toTouristDestination(): Any =
    when (type) {
        NotificationType.CHAT_MESSAGE ->
            chatId?.let(ChatDestination::Detail) ?: TouristDestination.Home
        NotificationType.RESERVATION_CONFIRMED,
        NotificationType.RESERVATION_CANCELLED,
        NotificationType.TOUR_CANCELLED,
        NotificationType.TOUR_COMPLETED,
        NotificationType.REVIEW_REQUEST,
        NotificationType.UPCOMING_TOUR_REMINDER,
        -> reservationId?.let(TouristDestination::ReservationDetail) ?: TouristDestination.Trips
        NotificationType.PAYMENT_SUCCEEDED,
        NotificationType.PAYMENT_FAILED,
        NotificationType.REFUND_REQUESTED,
        NotificationType.REFUND_COMPLETED,
        NotificationType.REFUND_FAILED,
        NotificationType.REFUND_MANUAL_REVIEW,
        ->
            paymentId?.let {
                TouristPaymentDestination.Status(
                    paymentId = it,
                    openHostedIfRequired = false,
                )
            } ?: TouristPaymentDestination.WalletTransactions
        NotificationType.TOUR_APPROVED,
        NotificationType.TOUR_CHANGE_APPROVED,
        -> sessionId?.let(TouristDestination::TourDetail) ?: TouristDestination.Home
        NotificationType.SECURITY_ALERT -> TouristDestination.Profile
        NotificationType.TOUR_REJECTED,
        NotificationType.TOUR_CHANGE_REJECTED,
        NotificationType.TOUR_PURCHASED,
        NotificationType.RATING_RECEIVED,
        NotificationType.COMMENT_RECEIVED,
        NotificationType.EARNING_AVAILABLE,
        NotificationType.WITHDRAWAL_COMPLETED,
        NotificationType.UNKNOWN,
        -> TouristDestination.Home
    }

private fun NotificationNavigationTarget.guideTourDestinationOrFallback(): Any =
    if (!tourId.isNullOrBlank() && !sessionId.isNullOrBlank()) {
        GuideTourDestination.Detail(tourId = tourId, sessionId = sessionId)
    } else {
        GuideTourDestination.MyTours
    }

internal fun Any.marksNotificationAfterSuccessfulLoad(): Boolean =
    this is ChatDestination.Detail ||
        this is GuideTourDestination.Detail ||
        this is TouristDestination.TourDetail ||
        this is TouristDestination.ReservationDetail ||
        this is TouristPaymentDestination.Status
