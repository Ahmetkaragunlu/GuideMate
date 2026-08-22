package com.ahmetkaragunlu.guidemate.tour.data.mock

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.Tour
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourReview
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourCatalogState
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import java.time.Instant

internal const val MOCK_CURRENT_GUIDE_ID = "guide-current"

internal fun createMockTourCatalogState(): TourCatalogState {
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
    val currentGuide =
        GuidePublicSummary(
            id = MOCK_CURRENT_GUIDE_ID,
            displayName = "Ahmet Yılmaz",
            profileImageResId = R.drawable.unnamed,
        )
    val tours =
        listOf(
            Tour(
                id = "tour-kapadokya",
                guide = currentGuide,
                title = "Kapadokya Balon Turu",
                description = "Kapadokya'nın vadilerini ve tarihi dokusunu yerel hikayeler eşliğinde keşfedin.",
                country = "Türkiye",
                city = "Nevşehir, Ürgüp",
                timeZoneId = "Europe/Istanbul",
                category = TourCategory.ADVENTURE,
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
                guide = currentGuide,
                title = "Efes Antik Kent",
                description = "Efes'in antik sokaklarını tarihsel bağlamı ve mimari ayrıntılarıyla gezin.",
                country = "Türkiye",
                city = "İzmir, Selçuk",
                timeZoneId = "Europe/Istanbul",
                category = TourCategory.CULTURE,
                languages = languagesTrEn,
                coverImageResId = R.drawable.example,
                approvalStatus = TourApprovalStatus.APPROVED,
                publishedAt = Instant.parse("2026-05-20T09:00:00Z"),
            ),
            Tour(
                id = "tour-ayasofya",
                guide = currentGuide,
                title = "Ayasofya Gizli Tarih Turu",
                description = "Ayasofya ve çevresindeki tarihi noktaları yerel hikayeler eşliğinde keşfedin.",
                country = "Türkiye",
                city = "İstanbul",
                timeZoneId = "Europe/Istanbul",
                category = TourCategory.CULTURE,
                languages = languagesTrEn,
                coverImageResId = R.drawable.example,
                approvalStatus = TourApprovalStatus.APPROVED,
                publishedAt = Instant.parse("2026-06-10T09:00:00Z"),
                averageRating = 4.9,
                reviewCount = 76,
            ),
            Tour(
                id = "tour-bogaz",
                guide = currentGuide,
                title = "İstanbul Boğaz Turu",
                description = "Boğaz kıyısındaki yapıları ve İstanbul'un dönüşümünü keşfedin.",
                country = "Türkiye",
                city = "İstanbul",
                timeZoneId = "Europe/Istanbul",
                category = TourCategory.ENTERTAINMENT,
                languages = languagesTrEn,
                coverImageResId = R.drawable.example,
                approvalStatus = TourApprovalStatus.APPROVED,
                publishedAt = Instant.parse("2025-12-01T09:00:00Z"),
                averageRating = 4.8,
                reviewCount = 210,
            ),
            Tour(
                id = "tour-pamukkale",
                guide = currentGuide,
                title = "Pamukkale Gezisi",
                description = "Pamukkale travertenleri ve Hierapolis antik kentini birlikte keşfedin.",
                country = "Türkiye",
                city = "Denizli",
                timeZoneId = "Europe/Istanbul",
                category = TourCategory.NATURE,
                languages = languagesTrEn,
                coverImageResId = R.drawable.example,
                approvalStatus = TourApprovalStatus.APPROVED,
                publishedAt = Instant.parse("2025-10-01T09:00:00Z"),
                averageRating = 5.0,
                reviewCount = 45,
            ),
            Tour(
                id = "tour-sultanahmet-pending",
                guide = currentGuide,
                title = "Sultanahmet ve Gizli Sokaklar",
                description = "Sultanahmet çevresindeki az bilinen tarihi noktaları keşfedin.",
                country = "Türkiye",
                city = "İstanbul",
                timeZoneId = "Europe/Istanbul",
                category = TourCategory.CULTURE,
                languages = languagesTrEn,
                coverImageResId = R.drawable.example,
                approvalStatus = TourApprovalStatus.PENDING_REVIEW,
                approvalSubmittedAt = Instant.parse("2026-07-12T14:30:00Z"),
            ),
            Tour(
                id = "tour-mardin-rejected",
                guide = currentGuide,
                title = "Mardin Taş Evleri",
                description = "Mardin'in taş mimarisini ve çok kültürlü geçmişini keşfedin.",
                country = "Türkiye",
                city = "Mardin",
                timeZoneId = "Europe/Istanbul",
                category = TourCategory.CULTURE,
                languages = languagesTrEn,
                coverImageResId = R.drawable.example,
                approvalStatus = TourApprovalStatus.REJECTED,
                approvalSubmittedAt = Instant.parse("2026-07-10T09:15:00Z"),
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
                priceMinor = 150_000,
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
                priceMinor = 80_000,
                capacity = 10,
                bookedCount = 5,
                status = TourSessionStatus.OPEN_FOR_BOOKING,
            ),
            TourSession(
                id = "session-ayasofya-active",
                tourId = "tour-ayasofya",
                meetingPoint = "Ayasofya Meydanı ana giriş kapısının önü.",
                startsAt = Instant.parse("2027-07-05T08:00:00Z"),
                durationMinutes = 180,
                priceMinor = 110_000,
                capacity = 15,
                bookedCount = 4,
                status = TourSessionStatus.OPEN_FOR_BOOKING,
            ),
            TourSession(
                id = "session-bogaz-past",
                tourId = "tour-bogaz",
                meetingPoint = "Eminönü vapur iskelesi.",
                startsAt = Instant.parse("2026-01-12T09:00:00Z"),
                durationMinutes = 120,
                priceMinor = 60_000,
                capacity = 10,
                bookedCount = 10,
                status = TourSessionStatus.COMPLETED,
                earningsMinor = 600_000,
            ),
            TourSession(
                id = "session-pamukkale-cancelled",
                tourId = "tour-pamukkale",
                meetingPoint = "Pamukkale güney giriş kapısı.",
                startsAt = Instant.parse("2025-11-05T08:00:00Z"),
                durationMinutes = 300,
                priceMinor = 90_000,
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
                priceMinor = 100_000,
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
                priceMinor = 120_000,
                capacity = 8,
                bookedCount = 0,
                status = TourSessionStatus.CLOSED,
            ),
        )
    return TourCatalogState(tours = tours, sessions = sessions)
}
