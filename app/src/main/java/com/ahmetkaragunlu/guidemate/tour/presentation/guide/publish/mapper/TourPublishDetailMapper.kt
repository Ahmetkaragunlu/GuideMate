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
                title = tourName,
                imageResId = previewImageResId,
                imageUrl = selectedCoverImageUri,
                location = locationDisplay,
                languagesFlag = spokenLanguages.joinToString(separator = " ") { it.flagEmoji },
                languagesText = spokenLanguages.joinToString(separator = ", ") { it.shortCode },
                category = category,
                description = tourDescription,
            ),
        session =
            TourDetailSessionUiState(
                date =
                    listOfNotNull(
                        tourDate?.format(localizedDateFormatter()),
                        startTime?.format(localizedTimeFormatter()),
                    ).joinToString(" "),
                durationMinutes = durationMinutes ?: 0,
                priceMinor = price.toCurrencyMinorUnitsOrNull() ?: 0,
                capacity = capacity.toIntOrNull() ?: 0,
                meetingPoint = meetingPoint,
            ),
        guide =
            TourDetailGuideUiState(
                name = guideName,
                imageResId = guideImageResId,
                imageUrl = guideImageUrl,
            ),
    )

private fun localizedDateFormatter(): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault())

private fun localizedTimeFormatter(): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault())
