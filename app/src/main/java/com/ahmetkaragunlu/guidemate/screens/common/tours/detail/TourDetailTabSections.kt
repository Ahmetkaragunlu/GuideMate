package com.ahmetkaragunlu.guidemate.screens.common.tours.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailReviewUiModel
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailTab
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailUiState

@Composable
internal fun TourDetailTabContent(
    selectedTab: TourDetailTab,
    uiState: TourDetailUiState,
) {
    var isDetailsExpanded by rememberSaveable { mutableStateOf(false) }
    var isMeetingExpanded by rememberSaveable { mutableStateOf(false) }
    var expandedReviewIds by rememberSaveable { mutableStateOf(emptyList<String>()) }
    val detailsScrollState = rememberScrollState()
    val meetingScrollState = rememberScrollState()
    val reviewsScrollState = rememberScrollState()

    when (selectedTab) {
        TourDetailTab.REVIEWS -> {
            ReviewsSection(
                expandedReviewIds = expandedReviewIds,
                onExpandReview = { reviewId ->
                    if (!expandedReviewIds.contains(reviewId)) {
                        expandedReviewIds = expandedReviewIds + reviewId
                    }
                },
                reviewsScrollState = reviewsScrollState,
                reviews = uiState.reviews,
            )
        }

        TourDetailTab.DETAILS -> {
            ExpandableTextSection(
                text = uiState.description,
                isExpanded = isDetailsExpanded,
                onExpand = { isDetailsExpanded = true },
                textScrollState = detailsScrollState,
            )
        }

        TourDetailTab.MEETING -> {
            ExpandableTextSection(
                text = uiState.meetingPoint,
                isExpanded = isMeetingExpanded,
                onExpand = { isMeetingExpanded = true },
                textScrollState = meetingScrollState,
            )
        }
    }
}

@Composable
private fun ReviewsSection(
    expandedReviewIds: List<String>,
    onExpandReview: (String) -> Unit,
    reviewsScrollState: ScrollState,
    reviews: List<TourDetailReviewUiModel>,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(170.dp)
                .verticalScroll(reviewsScrollState)
                .padding(vertical = dimensionResource(R.dimen.spacing_medium)),
    ) {
        if (reviews.isEmpty()) {
            Text(
                text = stringResource(R.string.no_reviews_yet),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_color),
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))) {
                reviews.forEachIndexed { index, review ->
                    key(review.id) {
                        TourReviewItem(
                            review = review,
                            isExpanded = expandedReviewIds.contains(review.id),
                            onExpand = { onExpandReview(review.id) },
                        )
                        if (index != reviews.lastIndex) {
                            TourDetailSectionDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TourReviewItem(
    review: TourDetailReviewUiModel,
    isExpanded: Boolean,
    onExpand: () -> Unit,
) {
    val shouldShowReadMore = review.comment.length > 120
    val reviewScrollState = rememberScrollState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        verticalAlignment = Alignment.Top,
    ) {
        Image(
            painter = painterResource(id = review.reviewerImageResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(36.dp)
                    .clip(CircleShape),
        )
        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny))) {
            Text(
                text = review.reviewerName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
            if (isExpanded) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .verticalScroll(reviewScrollState),
                ) {
                    Text(
                        text = review.comment,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(R.color.text_color),
                    )
                }
            } else {
                Text(
                    text = review.comment,
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
                        modifier = Modifier.clickable(onClick = onExpand),
                    )
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
