package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.mapper

import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun GuideTourPublishUiState.toPreviewDetailUiState(): TourDetailUiState =
    TourDetailUiState(
        title = tourName.ifBlank { "Ayasofya Gizli Tarih Turu" },
        imageResId = previewImageResId,
        imageUrl = selectedCoverImageUri,
        date =
            listOfNotNull(
                tourDate?.format(localizedDateFormatter()),
                startTime?.format(localizedTimeFormatter()),
            ).joinToString(" "),
        durationMinutes = durationMinutes ?: 0,
        location = locationDisplay,
        languagesFlag = spokenLanguages.joinToString(separator = " ") { it.flagEmoji },
        languagesText = spokenLanguages.joinToString(separator = ", ") { it.shortCode },
        category = category,
        price = price.toDoubleOrNull() ?: 1500.0,
        capacity = capacity.toIntOrNull() ?: 0,
        description =
            tourDescription.ifBlank {
                "Bu turda Ayasofya ve çevresindeki tarihi noktaları yerel bir bakış açısıyla keşfedeceksiniz."
            },
        meetingPoint =
            meetingPoint.ifBlank {
                "Buluşma noktası: Ayasofya Meydanı ana giriş kapısı önü."
            },
        guideName = guideName,
        guideImageResId = guideImageResId,
    )

private fun localizedDateFormatter(): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault())

private fun localizedTimeFormatter(): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault())
