package com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.mapper

import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyMinorUnitsOrNull
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailGuideUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailSessionUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailTourUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishUiState
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun GuideTourPublishUiState.toPreviewDetailUiState(): TourDetailUiState =
    TourDetailUiState(
        tour =
            TourDetailTourUiState(
                title = content.tourName,
                imageResId = content.previewImageResId,
                imageUrl = content.selectedCoverImageUri,
                location = locationDisplay,
                languagesFlag = content.spokenLanguages.joinToString(separator = " ") { it.flagEmoji },
                languagesText = content.spokenLanguages.joinToString(separator = ", ") { it.shortCode },
                category = content.category,
                description = content.tourDescription,
            ),
        session =
            TourDetailSessionUiState(
                date =
                    listOfNotNull(
                        session.tourDate?.format(localizedDateFormatter()),
                        session.startTime?.format(localizedTimeFormatter()),
                    ).joinToString(" "),
                durationMinutes = session.durationMinutes ?: 0,
                priceMinor = session.price.toCurrencyMinorUnitsOrNull() ?: 0,
                capacity = session.capacity.toIntOrNull() ?: 0,
                meetingPoint = session.meetingPoint,
            ),
        guide =
            TourDetailGuideUiState(
                name = guide.name,
                imageResId = guide.imageResId,
                imageUrl = guide.imageUrl,
            ),
    )

private fun localizedDateFormatter(): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault())

private fun localizedTimeFormatter(): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault())
