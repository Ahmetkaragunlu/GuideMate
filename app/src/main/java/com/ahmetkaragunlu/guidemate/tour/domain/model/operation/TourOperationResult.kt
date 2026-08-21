package com.ahmetkaragunlu.guidemate.tour.domain.model.operation

enum class TourOperationResult {
    SUCCESS,
    SESSION_NOT_FOUND,
    TOUR_NOT_FOUND,
    SESSION_ALREADY_STARTED,
    TOUR_NOT_APPROVED,
    CAPACITY_FULL,
    STATUS_NOT_MANAGEABLE,
    TOUR_NOT_ARCHIVABLE,
}
