package com.ahmetkaragunlu.guidemate.navigation.notification

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.navigation.chat.ChatDestination
import com.ahmetkaragunlu.guidemate.navigation.guide.tours.GuideTourDestination
import com.ahmetkaragunlu.guidemate.navigation.tourist.TouristDestination
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.TouristPaymentDestination
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetType
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationVisibleTarget

internal fun NavBackStackEntry?.toGuideVisibleNotificationTarget(): NotificationVisibleTarget? =
    when {
        this == null -> null
        destination.hasRoute<ChatDestination.Detail>() ->
            toRoute<ChatDestination.Detail>().chatId.asVisibleTarget(NotificationTargetType.CHAT)
        destination.hasRoute<GuideTourDestination.Detail>() ->
            toRoute<GuideTourDestination.Detail>().tourId.asVisibleTarget(NotificationTargetType.TOUR)
        else -> null
    }

internal fun NavBackStackEntry?.toTouristVisibleNotificationTarget(): NotificationVisibleTarget? =
    when {
        this == null -> null
        destination.hasRoute<ChatDestination.Detail>() ->
            toRoute<ChatDestination.Detail>().chatId.asVisibleTarget(NotificationTargetType.CHAT)
        destination.hasRoute<TouristDestination.TourDetail>() ->
            toRoute<TouristDestination.TourDetail>().sessionId.asVisibleTarget(NotificationTargetType.TOUR)
        destination.hasRoute<TouristDestination.ReservationDetail>() ->
            toRoute<TouristDestination.ReservationDetail>().reservationId.asVisibleTarget(
                NotificationTargetType.RESERVATION,
            )
        destination.hasRoute<TouristPaymentDestination.Hosted>() ->
            toRoute<TouristPaymentDestination.Hosted>().paymentId.asVisibleTarget(
                NotificationTargetType.PAYMENT,
            )
        destination.hasRoute<TouristPaymentDestination.Status>() ->
            toRoute<TouristPaymentDestination.Status>().paymentId.asVisibleTarget(
                NotificationTargetType.PAYMENT,
            )
        destination.hasRoute<TouristPaymentDestination.Success>() ->
            toRoute<TouristPaymentDestination.Success>().paymentId.asVisibleTarget(
                NotificationTargetType.PAYMENT,
            )
        else -> null
    }

private fun String.asVisibleTarget(type: NotificationTargetType): NotificationVisibleTarget =
    NotificationVisibleTarget(type = type, targetId = this)
