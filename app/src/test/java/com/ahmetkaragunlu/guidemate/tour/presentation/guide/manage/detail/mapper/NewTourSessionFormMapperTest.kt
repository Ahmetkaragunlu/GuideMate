package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail.mapper

import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail.model.NewTourSessionFormState
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NewTourSessionFormMapperTest {
    @Test
    fun validFormMapsToCanonicalSessionInput() {
        val input =
            NewTourSessionFormState(
                timeZoneId = "Europe/Istanbul",
                selectedDate = LocalDate.of(2027, 5, 24),
                selectedTime = LocalTime.of(12, 30),
                durationMinutes = 180,
                meetingPoint = "  Main square  ",
                price = "125",
                capacity = "12",
            ).toTourSessionInputOrNull()

        requireNotNull(input)
        assertEquals("Main square", input.meetingPoint)
        assertEquals(Instant.parse("2027-05-24T09:30:00Z"), input.startsAt)
        assertEquals(180, input.durationMinutes)
        assertEquals(12_500L, input.priceMinor)
        assertEquals(12, input.capacity)
    }

    @Test
    fun invalidOrIncompleteFormDoesNotCreateSessionInput() {
        val completeForm =
            NewTourSessionFormState(
                timeZoneId = "Europe/Istanbul",
                selectedDate = LocalDate.of(2027, 5, 24),
                selectedTime = LocalTime.of(12, 30),
                durationMinutes = 180,
                meetingPoint = "Main square",
                price = "125",
                capacity = "12",
            )

        assertNull(completeForm.copy(timeZoneId = "Invalid/Zone").toTourSessionInputOrNull())
        assertNull(completeForm.copy(selectedDate = null).toTourSessionInputOrNull())
    }
}
