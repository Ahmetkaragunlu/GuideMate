package com.ahmetkaragunlu.guidemate.tour.domain.model.discovery

data class TourSearchQuery(
    val text: String? = null,
    val countryCode: String? = null,
    val cityPlaceId: String? = null,
    val categoryCode: String? = null,
    val languageCodes: List<String> = emptyList(),
    val minimumRating: Double? = null,
    val minimumPriceMinor: Long? = null,
    val maximumPriceMinor: Long? = null,
    val sort: TourSearchSort = TourSearchSort.STARTS_AT_ASC,
)

enum class TourSearchSort {
    STARTS_AT_ASC,
    RATING_DESC,
    PRICE_ASC,
    PRICE_DESC,
}
