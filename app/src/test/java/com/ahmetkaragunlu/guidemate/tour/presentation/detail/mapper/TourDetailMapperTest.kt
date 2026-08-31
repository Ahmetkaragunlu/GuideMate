package com.ahmetkaragunlu.guidemate.tour.presentation.detail.mapper

import com.ahmetkaragunlu.guidemate.testing.testTourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import org.junit.Assert.assertEquals
import org.junit.Test

class TourDetailMapperTest {
    @Test
    fun `detail keeps guide id for public profile navigation`() {
        val details = testTourDetails()

        val uiState =
            TourWithSession(
                tour = details.tour,
                session = details.sessions.single(),
            ).toTourDetailUiState()

        assertEquals(details.tour.guide.id, uiState.guideId)
    }
}
