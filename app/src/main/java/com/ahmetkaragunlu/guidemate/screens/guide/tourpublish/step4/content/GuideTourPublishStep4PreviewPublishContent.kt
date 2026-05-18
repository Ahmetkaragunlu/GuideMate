package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.screens.common.tab.GuideMateTabRow
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.components.GuideTourPublishStepProgress
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPreviewTab
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.mock.PREVIEW_ABOUT_TEXT_FALLBACK
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.mock.PREVIEW_CATEGORY_FALLBACK
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.mock.PREVIEW_DATE_FALLBACK
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.mock.PREVIEW_GUIDE_NAME
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.mock.PREVIEW_GUIDE_ROLE
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.mock.PREVIEW_LANGUAGES_CODES_FALLBACK
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.mock.PREVIEW_LOCATION_FALLBACK
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.mock.PREVIEW_MEETING_TEXT_FALLBACK
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.mock.PREVIEW_REVIEWS
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.mock.PREVIEW_REVIEW_META
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.mock.PREVIEW_TOUR_TITLE_FALLBACK
import compose.icons.TablerIcons
import compose.icons.tablericons.Rocket

@Composable
fun GuideTourPublishStep4PreviewPublishContent(
    uiState: GuideTourPublishUiState,
    onPublish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTab by rememberSaveable { mutableStateOf(GuideTourPreviewTab.DETAILS) }
    var isDetailsExpanded by rememberSaveable { mutableStateOf(false) }
    var isMeetingExpanded by rememberSaveable { mutableStateOf(false) }
    var expandedReviewIndexes by rememberSaveable { mutableStateOf(emptyList<Int>()) }
    val detailsScrollState = rememberScrollState()
    val meetingScrollState = rememberScrollState()
    val reviewsScrollState = rememberScrollState()

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
    ) {
        Column(
            modifier =
                Modifier
                    .widthIn(max = 380.dp)
                    .fillMaxWidth(),
        ) {
            GuideTourPublishStepProgress(
                progressLabelResId = R.string.guide_tour_publish_step4_progress_label,
                filledStepIndexInclusive = 3,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_medium)),
            )
            PreviewBanner()
            HeroSection(uiState = uiState)
            SectionDivider()
            DateLocationRow(uiState = uiState)
            SectionDivider()
            LanguageCategoryRow(uiState = uiState)
            SectionDivider()
            PriceRow(uiState = uiState)
            SectionDivider()
            GuideInfoRow()

            GuideMateTabRow(
                tabs = GuideTourPreviewTab.entries,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
            )

            when (selectedTab) {
                GuideTourPreviewTab.REVIEWS -> {
                    ReviewsSection(
                        expandedReviewIndexes = expandedReviewIndexes,
                        onExpandReview = { index ->
                            if (!expandedReviewIndexes.contains(index)) {
                                expandedReviewIndexes = expandedReviewIndexes + index
                            }
                        },
                        reviewsScrollState = reviewsScrollState,
                    )
                }

                GuideTourPreviewTab.DETAILS -> {
                    ExpandableTextSection(
                        text = uiState.tourDescription.ifBlank { PREVIEW_ABOUT_TEXT_FALLBACK },
                        isExpanded = isDetailsExpanded,
                        onExpand = { isDetailsExpanded = true },
                        textScrollState = detailsScrollState,
                    )
                }

                GuideTourPreviewTab.MEETING -> {
                    ExpandableTextSection(
                        text = uiState.meetingPoint.ifBlank { PREVIEW_MEETING_TEXT_FALLBACK },
                        isExpanded = isMeetingExpanded,
                        onExpand = { isMeetingExpanded = true },
                        textScrollState = meetingScrollState,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
        EditButton(
            text = R.string.guide_tour_publish_step4_publish_button,
            onClick = onPublish,
            icon = TablerIcons.Rocket,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_extra_large)),
        )
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 1.dp)
}

@Composable
private fun PreviewBanner() {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.spacing_medium))
                .background(
                    color = Color(0xFFF4F7FC),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
                )
                .padding(
                    horizontal = dimensionResource(R.dimen.spacing_medium),
                    vertical = dimensionResource(R.dimen.spacing_small),
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.guide_tour_publish_step4_preview_banner),
            style = MaterialTheme.typography.labelLarge,
            color = colorResource(R.color.text_color),
        )
    }
}

@Composable
private fun HeroSection(
    uiState: GuideTourPublishUiState,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(top = dimensionResource(R.dimen.spacing_medium)),
    ) {
        Image(
            painter = painterResource(id = uiState.previewImageResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        brush =
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.72f),
                                    ),
                            ),
                    ),
        )
        Column(
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(dimensionResource(R.dimen.spacing_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
        ) {
            Text(
                text = uiState.tourName.ifBlank { PREVIEW_TOUR_TITLE_FALLBACK },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "⭐ $PREVIEW_REVIEW_META",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
private fun DateLocationRow(
    uiState: GuideTourPublishUiState,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_medium)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        EmojiLabelItem(
            emoji = "📅",
            label = uiState.tourDate.ifBlank { PREVIEW_DATE_FALLBACK },
        )
        EmojiLabelItem(
            emoji = "🌍",
            label = uiState.locationDisplay.ifBlank { PREVIEW_LOCATION_FALLBACK },
        )
    }
}

@Composable
private fun LanguageCategoryRow(
    uiState: GuideTourPublishUiState,
) {
    val languagesFlags = uiState.spokenLanguages.joinToString(separator = " ") { it.flagEmoji }
    val languagesCodes = uiState.spokenLanguages.joinToString(separator = ", ") { it.previewCode }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_medium)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        EmojiLabelItem(
            emoji = languagesFlags.ifBlank { "🇹🇷 🇬🇧" },
            label = languagesCodes.ifBlank { PREVIEW_LANGUAGES_CODES_FALLBACK },
        )
        EmojiLabelItem(
            emoji = "🏷️",
            label = uiState.category.ifBlank { PREVIEW_CATEGORY_FALLBACK },
        )
    }
}

@Composable
private fun EmojiLabelItem(
    emoji: String,
    label: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = emoji)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun PriceRow(
    uiState: GuideTourPublishUiState,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_medium)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text =
                if (uiState.price.isBlank()) {
                    "1500 ₺"
                } else {
                    "${uiState.price} ₺"
                },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.brand_color),
        )
    }
}

@Composable
private fun GuideInfoRow() {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_medium)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.unnamed),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(42.dp)
                        .clip(CircleShape),
            )
            Column {
                Text(
                    text = PREVIEW_GUIDE_NAME,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = PREVIEW_GUIDE_ROLE,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.text_color),
                )
            }
        }
        Text(
            text = stringResource(R.string.guide_tour_publish_step4_view_profile),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.brand_color),
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun ReviewsSection(
    expandedReviewIndexes: List<Int>,
    onExpandReview: (Int) -> Unit,
    reviewsScrollState: ScrollState,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(170.dp)
                .verticalScroll(reviewsScrollState)
                .padding(vertical = dimensionResource(R.dimen.spacing_medium)),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))) {
            PREVIEW_REVIEWS.forEachIndexed { index, (reviewerName, reviewText) ->
                val isExpanded = expandedReviewIndexes.contains(index)
                val shouldShowReadMore = reviewText.length > 120

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                    verticalAlignment = Alignment.Top,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.unnamed),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .size(36.dp)
                                .clip(CircleShape),
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny))) {
                        Text(
                            text = reviewerName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        if (isExpanded) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(72.dp)
                                        .verticalScroll(rememberScrollState()),
                            ) {
                                Text(
                                    text = reviewText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorResource(R.color.text_color),
                                )
                            }
                        } else {
                            Text(
                                text = reviewText,
                                style = MaterialTheme.typography.bodySmall,
                                color = colorResource(R.color.text_color),
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                            )
                            if (shouldShowReadMore) {
                                Text(
                                    text = stringResource(R.string.preview_read_more),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(R.color.brand_color),
                                    modifier = Modifier.clickable { onExpandReview(index) },
                                )
                            }
                        }
                    }
                }
                if (index != PREVIEW_REVIEWS.lastIndex) {
                    SectionDivider()
                }
            }
        }
    }
}

@Composable
private fun ExpandableTextSection(
    text: String,
    isExpanded: Boolean,
    onExpand: () -> Unit,
    textScrollState: ScrollState,
) {
    val shouldShowReadMore = text.length > 140

    Column(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_medium))) {
        if (isExpanded) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .verticalScroll(textScrollState),
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.text_color),
                )
            }
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_color),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
            )
            if (shouldShowReadMore) {
                Text(
                    text = stringResource(R.string.preview_read_more),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.brand_color),
                    modifier =
                        Modifier
                            .padding(top = dimensionResource(R.dimen.spacing_tiny))
                            .clickable(onClick = onExpand),
                )
            }
        }
    }
}
