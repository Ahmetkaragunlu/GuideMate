package com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.mapper

import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyMinorUnitsOrNull
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishUiState
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
        priceMinor = price.toCurrencyMinorUnitsOrNull() ?: 150_000,
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
        guideImageUrl = guideImageUrl,
    )

private fun localizedDateFormatter(): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault())

private fun localizedTimeFormatter(): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault())
