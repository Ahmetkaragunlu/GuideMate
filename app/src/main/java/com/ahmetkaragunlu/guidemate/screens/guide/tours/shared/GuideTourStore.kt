package com.ahmetkaragunlu.guidemate.screens.guide.tours.shared

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.CancelTourSessionRequest
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.CreateTourSessionRequest
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourCatalogState
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.Tour
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourEditRequest
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourLanguage
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourReview
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourSession
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourSessionStatus
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class GuideTourStore
    @Inject
    constructor() {
        private val _state = MutableStateFlow(createMockState())
        val state: StateFlow<GuideTourCatalogState> = _state.asStateFlow()

        fun submitForReview(
            tour: Tour,
            session: TourSession,
        ): Boolean {
            val current = _state.value
            val now = Instant.now()
            if (
                tour.id != session.tourId ||
                    current.tours.any { it.id == tour.id } ||
                    current.sessions.any { it.id == session.id } ||
                    tour.title.isBlank() ||
                    tour.description.isBlank() ||
                    session.meetingPoint.isBlank() ||
                    tour.country.isBlank() ||
                    tour.city.isBlank() ||
                    tour.category.isBlank() ||
                    tour.languages.isEmpty() ||
                    tour.approvalStatus != TourApprovalStatus.PENDING_REVIEW ||
                    tour.publishedAt != null ||
                    session.status != TourSessionStatus.CLOSED ||
                    session.bookedCount != 0 ||
                    !session.startsAt.isAfter(now) ||
                    session.durationMinutes <= 0 ||
                    session.price <= 0 ||
                    session.capacity <= 0
            ) {
                return false
            }

            val guideTourIds = current.guideTourIds(tour.guideId)
            val overlaps =
                current.sessions.any { existingSession ->
                    existingSession.tourId in guideTourIds &&
                        current.isScheduleBlocking(existingSession) &&
                        session.startsAt < existingSession.endsAt &&
                        session.endsAt > existingSession.startsAt
                }
            if (overlaps) return false

            _state.update { current ->
                current.copy(
                    tours = current.tours + tour.copy(reviewSubmittedAt = now),
                    sessions = current.sessions + session,
                )
            }
            return true
        }

        fun setSessionBookingOpen(
            sessionId: String,
            isOpen: Boolean,
        ): Boolean {
            val current = _state.value.findBySessionId(sessionId) ?: return false
            val now = Instant.now()
            if (!current.session.startsAt.isAfter(now)) return false
            val canOpen =
                current.tour.approvalStatus == TourApprovalStatus.APPROVED &&
                    current.session.bookedCount < current.session.capacity
            if (isOpen && !canOpen) return false
            if (!current.session.status.isBookingManageable) return false

            updateSession(sessionId) { session ->
                session.copy(
                    status =
                        if (isOpen) {
                            TourSessionStatus.OPEN_FOR_BOOKING
                        } else {
                            TourSessionStatus.CLOSED
                        },
                )
            }
            return true
        }

        fun updateTour(
            tourId: String,
            sessionId: String,
            request: TourEditRequest,
        ): Boolean {
            val catalog = _state.value
            val current = catalog.findBySessionId(sessionId) ?: return false
            val now = Instant.now()
            if (current.tour.id != tourId) return false
            if (!current.session.status.isBookingManageable) return false

            val locationChanged =
                request.country.trim() != current.tour.country ||
                    request.city.trim() != current.tour.city
            val categoryChanged = request.category.trim() != current.tour.category
            if (current.tour.publishedAt != null && locationChanged) return false
            if (
                current.session.bookedCount > 0 &&
                    (
                        request.startsAt != current.session.startsAt ||
                            request.durationMinutes != current.session.durationMinutes ||
                            request.meetingPoint.trim() != current.session.meetingPoint
                    )
            ) {
                return false
            }
            if (
                request.title.isBlank() ||
                    request.description.isBlank() ||
                    request.country.isBlank() ||
                    request.city.isBlank() ||
                    request.category.isBlank() ||
                    request.languages.isEmpty() ||
                    request.meetingPoint.isBlank() ||
                    !request.startsAt.isAfter(now) ||
                    request.durationMinutes <= 0 ||
                    request.price <= 0 ||
                    request.capacity <= 0
            ) {
                return false
            }
            if (request.capacity < current.session.bookedCount) return false

            val requestEndsAt = request.startsAt.plusSeconds(request.durationMinutes * 60L)
            val guideTourIds = catalog.guideTourIds(current.tour.guideId)
            val overlapsAnotherSession =
                catalog.sessions.any { session ->
                    session.id != sessionId &&
                        session.tourId in guideTourIds &&
                        catalog.isScheduleBlocking(session) &&
                        request.startsAt < session.endsAt &&
                        requestEndsAt > session.startsAt
                }
            if (overlapsAnotherSession) return false

            val requiresReview =
                current.tour.approvalStatus != TourApprovalStatus.APPROVED ||
                    request.title.trim() != current.tour.title ||
                    request.description.trim() != current.tour.description ||
                    categoryChanged ||
                    request.languages != current.tour.languages ||
                    request.selectedCoverImageUri != null

            _state.update { state ->
                state.copy(
                    tours =
                        state.tours.map { tour ->
                            if (tour.id == tourId) {
                                tour.copy(
                                    title = request.title.trim(),
                                    description = request.description.trim(),
                                    country = request.country.trim(),
                                    city = request.city.trim(),
                                    category = request.category.trim(),
                                    languages = request.languages,
                                    coverImageUrl =
                                        request.selectedCoverImageUri ?: tour.coverImageUrl,
                                    approvalStatus =
                                        if (requiresReview) {
                                            TourApprovalStatus.PENDING_REVIEW
                                        } else {
                                            tour.approvalStatus
                                        },
                                    rejectionReason = if (requiresReview) null else tour.rejectionReason,
                                    reviewSubmittedAt =
                                        if (requiresReview) {
                                            now
                                        } else {
                                            tour.reviewSubmittedAt
                                        },
                                )
                            } else {
                                tour
                            }
                        },
                    sessions =
                        state.sessions.map { session ->
                            if (session.id == sessionId) {
                                session.copy(
                                    meetingPoint = request.meetingPoint.trim(),
                                    startsAt = request.startsAt,
                                    durationMinutes = request.durationMinutes,
                                    price = request.price,
                                    capacity = request.capacity,
                                    status =
                                        if (requiresReview) {
                                            TourSessionStatus.CLOSED
                                        } else {
                                            session.status
                                        },
                                )
                            } else if (
                                requiresReview &&
                                    session.tourId == tourId &&
                                    session.startsAt.isAfter(now) &&
                                    session.status.isBookingManageable
                            ) {
                                session.copy(status = TourSessionStatus.CLOSED)
                            } else {
                                session
                            }
                        },
                )
            }
            return true
        }

        fun archiveRejectedTour(tourId: String): Boolean {
            val current = _state.value
            val tour = current.tours.firstOrNull { it.id == tourId } ?: return false
            if (
                tour.approvalStatus != TourApprovalStatus.REJECTED ||
                    tour.publishedAt != null
            ) {
                return false
            }

            _state.update { state ->
                state.copy(
                    tours =
                        state.tours.map { item ->
                            if (item.id == tourId) {
                                item.copy(approvalStatus = TourApprovalStatus.ARCHIVED)
                            } else {
                                item
                            }
                        },
                    sessions =
                        state.sessions.map { session ->
                            if (session.tourId == tourId && session.status.isBookingManageable) {
                                session.copy(status = TourSessionStatus.CLOSED)
                            } else {
                                session
                            }
                        },
                )
            }
            return true
        }

        fun cancelSession(request: CancelTourSessionRequest): Boolean {
            val current = _state.value.findBySessionId(request.sessionId) ?: return false
            if (
                !current.session.status.isBookingManageable ||
                    !current.session.startsAt.isAfter(Instant.now()) ||
                    request.reason.isBlank()
            ) {
                return false
            }

            updateSession(request.sessionId) { session ->
                session.copy(
                    status = TourSessionStatus.CANCELLED,
                    cancellationReason = request.reason.trim(),
                )
            }
            return true
        }

        fun addSession(request: CreateTourSessionRequest): Boolean {
            val state = _state.value
            val now = Instant.now()
            val tour = state.tours.firstOrNull { it.id == request.tourId } ?: return false
            if (tour.approvalStatus != TourApprovalStatus.APPROVED) return false
            if (
                request.meetingPoint.isBlank() ||
                !request.startsAt.isAfter(now) ||
                    request.durationMinutes <= 0 ||
                    request.price <= 0 ||
                    request.capacity <= 0
            ) {
                return false
            }

            val endsAt = request.startsAt.plusSeconds(request.durationMinutes * 60L)
            val guideTourIds = state.guideTourIds(tour.guideId)
            val overlaps =
                state.sessions.any { session ->
                    session.tourId in guideTourIds &&
                        state.isScheduleBlocking(session) &&
                        request.startsAt < session.endsAt &&
                        endsAt > session.startsAt
                }
            if (overlaps) return false

            val session =
                TourSession(
                    id = UUID.randomUUID().toString(),
                    tourId = request.tourId,
                    meetingPoint = request.meetingPoint.trim(),
                    startsAt = request.startsAt,
                    durationMinutes = request.durationMinutes,
                    price = request.price,
                    capacity = request.capacity,
                    bookedCount = 0,
                    status = TourSessionStatus.OPEN_FOR_BOOKING,
                )
            _state.update { current -> current.copy(sessions = current.sessions + session) }
            return true
        }

        private fun GuideTourCatalogState.guideTourIds(guideId: String): Set<String> =
            tours.filter { it.guideId == guideId }.mapTo(mutableSetOf()) { it.id }

        private fun GuideTourCatalogState.isScheduleBlocking(session: TourSession): Boolean {
            val approvalStatus = tours.firstOrNull { it.id == session.tourId }?.approvalStatus ?: return false
            return !session.status.isTerminal &&
                approvalStatus != TourApprovalStatus.REJECTED &&
                approvalStatus != TourApprovalStatus.ARCHIVED
        }

        private fun updateSession(
            sessionId: String,
            transform: (TourSession) -> TourSession,
        ) {
            _state.update { current ->
                current.copy(
                    sessions = current.sessions.map { session -> if (session.id == sessionId) transform(session) else session },
                )
            }
        }

        private companion object {
            fun createMockState(): GuideTourCatalogState {
                val languagesTrEn =
                    listOf(
                        TourLanguage("tr", "🇹🇷", "Türkçe", "TR"),
                        TourLanguage("en", "🇬🇧", "İngilizce", "EN"),
                    )
                val languagesTrDe =
                    listOf(
                        TourLanguage("de", "🇩🇪", "Almanca", "DE"),
                        TourLanguage("tr", "🇹🇷", "Türkçe", "TR"),
                    )
                val tours =
                    listOf(
                        Tour(
                            id = "tour-kapadokya",
                            guideId = "guide-current",
                            title = "Kapadokya Balon Turu",
                            description = "Kapadokya'nın vadilerini ve tarihi dokusunu yerel hikayeler eşliğinde keşfedin.",
                            country = "Türkiye",
                            city = "Nevşehir, Ürgüp",
                            timeZoneId = "Europe/Istanbul",
                            category = "Macera",
                            languages = languagesTrDe,
                            coverImageResId = R.drawable.example,
                            approvalStatus = TourApprovalStatus.APPROVED,
                            publishedAt = Instant.parse("2026-05-01T09:00:00Z"),
                            averageRating = 4.9,
                            reviewCount = 120,
                            recentReviews =
                                listOf(
                                    TourReview(
                                        id = "review-1",
                                        reviewerName = "Elif Demir",
                                        rating = 5,
                                        comment = "Rota ve anlatım çok başarılıydı. Rehberimiz bütün sorularımızı ayrıntılı şekilde yanıtladı.",
                                        reviewerImageResId = R.drawable.unnamed,
                                    ),
                                ),
                        ),
                        Tour(
                            id = "tour-efes",
                            guideId = "guide-current",
                            title = "Efes Antik Kent",
                            description = "Efes'in antik sokaklarını tarihsel bağlamı ve mimari ayrıntılarıyla gezin.",
                            country = "Türkiye",
                            city = "İzmir, Selçuk",
                            timeZoneId = "Europe/Istanbul",
                            category = "Tarih",
                            languages = languagesTrEn,
                            coverImageResId = R.drawable.example,
                            approvalStatus = TourApprovalStatus.APPROVED,
                            publishedAt = Instant.parse("2026-05-20T09:00:00Z"),
                        ),
                        Tour(
                            id = "tour-bogaz",
                            guideId = "guide-current",
                            title = "İstanbul Boğaz Turu",
                            description = "Boğaz kıyısındaki yapıları ve İstanbul'un dönüşümünü keşfedin.",
                            country = "Türkiye",
                            city = "İstanbul",
                            timeZoneId = "Europe/Istanbul",
                            category = "Tekne",
                            languages = languagesTrEn,
                            coverImageResId = R.drawable.example,
                            approvalStatus = TourApprovalStatus.APPROVED,
                            publishedAt = Instant.parse("2025-12-01T09:00:00Z"),
                            averageRating = 4.8,
                            reviewCount = 210,
                        ),
                        Tour(
                            id = "tour-pamukkale",
                            guideId = "guide-current",
                            title = "Pamukkale Gezisi",
                            description = "Pamukkale travertenleri ve Hierapolis antik kentini birlikte keşfedin.",
                            country = "Türkiye",
                            city = "Denizli",
                            timeZoneId = "Europe/Istanbul",
                            category = "Doğa",
                            languages = languagesTrEn,
                            coverImageResId = R.drawable.example,
                            approvalStatus = TourApprovalStatus.APPROVED,
                            publishedAt = Instant.parse("2025-10-01T09:00:00Z"),
                            averageRating = 5.0,
                            reviewCount = 45,
                        ),
                        Tour(
                            id = "tour-sultanahmet-pending",
                            guideId = "guide-current",
                            title = "Sultanahmet ve Gizli Sokaklar",
                            description = "Sultanahmet çevresindeki az bilinen tarihi noktaları keşfedin.",
                            country = "Türkiye",
                            city = "İstanbul",
                            timeZoneId = "Europe/Istanbul",
                            category = "Tarih ve Kültür",
                            languages = languagesTrEn,
                            coverImageResId = R.drawable.example,
                            approvalStatus = TourApprovalStatus.PENDING_REVIEW,
                            reviewSubmittedAt = Instant.parse("2026-07-12T14:30:00Z"),
                        ),
                        Tour(
                            id = "tour-mardin-rejected",
                            guideId = "guide-current",
                            title = "Mardin Taş Evleri",
                            description = "Mardin'in taş mimarisini ve çok kültürlü geçmişini keşfedin.",
                            country = "Türkiye",
                            city = "Mardin",
                            timeZoneId = "Europe/Istanbul",
                            category = "Tarih ve Kültür",
                            languages = languagesTrEn,
                            coverImageResId = R.drawable.example,
                            approvalStatus = TourApprovalStatus.REJECTED,
                            reviewSubmittedAt = Instant.parse("2026-07-10T09:15:00Z"),
                            rejectionReason = "Tur açıklamasını ve buluşma noktası bilgisini daha ayrıntılı yazmalısınız.",
                        ),
                    )

                val sessions =
                    listOf(
                        TourSession(
                            id = "session-kapadokya-active",
                            tourId = "tour-kapadokya",
                            meetingPoint = "Göreme merkez otobüs durağının önü.",
                            startsAt = Instant.parse("2027-05-24T06:00:00Z"),
                            durationMinutes = 180,
                            price = 1500.0,
                            capacity = 12,
                            bookedCount = 8,
                            status = TourSessionStatus.OPEN_FOR_BOOKING,
                        ),
                        TourSession(
                            id = "session-efes-active",
                            tourId = "tour-efes",
                            meetingPoint = "Efes Antik Kent üst giriş kapısı.",
                            startsAt = Instant.parse("2027-06-10T08:00:00Z"),
                            durationMinutes = 240,
                            price = 800.0,
                            capacity = 10,
                            bookedCount = 5,
                            status = TourSessionStatus.OPEN_FOR_BOOKING,
                        ),
                        TourSession(
                            id = "session-bogaz-past",
                            tourId = "tour-bogaz",
                            meetingPoint = "Eminönü vapur iskelesi.",
                            startsAt = Instant.parse("2026-01-12T09:00:00Z"),
                            durationMinutes = 120,
                            price = 600.0,
                            capacity = 10,
                            bookedCount = 10,
                            status = TourSessionStatus.COMPLETED,
                            earnings = 6000.0,
                        ),
                        TourSession(
                            id = "session-pamukkale-cancelled",
                            tourId = "tour-pamukkale",
                            meetingPoint = "Pamukkale güney giriş kapısı.",
                            startsAt = Instant.parse("2025-11-05T08:00:00Z"),
                            durationMinutes = 300,
                            price = 900.0,
                            capacity = 12,
                            bookedCount = 0,
                            status = TourSessionStatus.CANCELLED,
                            cancellationReason = "Olumsuz hava koşulları",
                        ),
                        TourSession(
                            id = "session-sultanahmet-pending",
                            tourId = "tour-sultanahmet-pending",
                            meetingPoint = "Sultanahmet tramvay durağı.",
                            startsAt = Instant.parse("2027-07-18T10:00:00Z"),
                            durationMinutes = 180,
                            price = 1000.0,
                            capacity = 10,
                            bookedCount = 0,
                            status = TourSessionStatus.CLOSED,
                        ),
                        TourSession(
                            id = "session-mardin-rejected",
                            tourId = "tour-mardin-rejected",
                            meetingPoint = "Eski Mardin PTT önü.",
                            startsAt = Instant.parse("2027-08-12T09:00:00Z"),
                            durationMinutes = 180,
                            price = 1200.0,
                            capacity = 8,
                            bookedCount = 0,
                            status = TourSessionStatus.CLOSED,
                        ),
                    )
                return GuideTourCatalogState(tours = tours, sessions = sessions)
            }
        }
    }
