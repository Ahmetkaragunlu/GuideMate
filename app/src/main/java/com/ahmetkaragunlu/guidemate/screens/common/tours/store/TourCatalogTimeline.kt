package com.ahmetkaragunlu.guidemate.screens.common.tours.store

import com.ahmetkaragunlu.guidemate.screens.common.tours.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.catalog.TourCatalogState
import java.time.Duration
import java.time.Instant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest

@OptIn(ExperimentalCoroutinesApi::class)
fun Flow<TourCatalogState>.refreshAtSessionTransitions(
    currentTime: () -> Instant = Instant::now,
): Flow<TourCatalogState> =
    transformLatest { catalog ->
        emit(catalog)

        while (true) {
            val now = currentTime()
            val nextTransition = catalog.nextVisibleSessionTransitionAfter(now) ?: break
            val waitMillis =
                Duration
                    .between(now, nextTransition)
                    .toMillis()
                    .coerceAtLeast(0L) + 1L

            delay(waitMillis)
            emit(catalog)
        }
    }

private fun TourCatalogState.nextVisibleSessionTransitionAfter(now: Instant): Instant? {
    val approvedTourIds =
        tours
            .asSequence()
            .filter { it.approvalStatus == TourApprovalStatus.APPROVED }
            .map { it.id }
            .toSet()

    return sessions
        .asSequence()
        .filter { session ->
            session.tourId in approvedTourIds && session.status.canToggleBookingAvailability
        }.flatMap { session -> sequenceOf(session.startsAt, session.endsAt) }
        .filter { transition -> transition.isAfter(now) }
        .minOrNull()
}
