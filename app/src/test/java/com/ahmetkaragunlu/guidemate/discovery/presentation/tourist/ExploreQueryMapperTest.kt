package com.ahmetkaragunlu.guidemate.discovery.presentation.tourist

import com.ahmetkaragunlu.guidemate.common.location.model.CityOption
import com.ahmetkaragunlu.guidemate.common.location.model.CountryOption
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.ExploreUiState
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.TourExploreState
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.TourFilterUiState
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ExploreQueryMapperTest {
    @Test
    fun `maps applied filters and prices to backend search query`() {
        val state =
            ExploreUiState(
                tours = TourExploreState(searchQuery = "museum"),
                appliedFilters =
                    TourFilterUiState(
                        selectedCategory = TourCategory.CULTURE,
                        selectedRating = 4,
                        priceRange = 12.34f..56.78f,
                        selectedCountry = CountryOption("TR", "Turkiye"),
                        selectedCity = CityOption("izmir-place", "Izmir", "TR"),
                        selectedLanguages = listOf(LanguageOption("tr", "Turkce", "TR")),
                    ),
            )

        val query = state.toTourSearchQuery()

        assertEquals("museum", query.text)
        assertEquals("TR", query.countryCode)
        assertEquals("izmir-place", query.cityPlaceId)
        assertEquals("culture", query.categoryCode)
        assertEquals(listOf("tr"), query.languageCodes)
        assertEquals(4.0, query.minimumRating)
        assertEquals(1_234L, query.minimumPriceMinor)
        assertEquals(5_678L, query.maximumPriceMinor)
    }

    @Test
    fun `default filters do not constrain backend query`() {
        val query = ExploreUiState().toTourSearchQuery()

        assertNull(query.minimumRating)
        assertNull(query.minimumPriceMinor)
        assertNull(query.maximumPriceMinor)
    }
}
