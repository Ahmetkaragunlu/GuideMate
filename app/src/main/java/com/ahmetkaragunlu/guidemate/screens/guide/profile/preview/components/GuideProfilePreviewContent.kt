package com.ahmetkaragunlu.guidemate.screens.guide.profile.preview.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.screens.common.tours.PopularTourCard
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.PopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.profile.components.ProfileStatsRow
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideSpokenLanguageUi
import com.ahmetkaragunlu.guidemate.screens.guide.profile.preview.model.GuideProfilePreviewUiState

@Composable
fun GuideProfilePreviewContent(
    uiState: GuideProfilePreviewUiState,
    modifier: Modifier = Modifier,
) {
    var isAboutExpanded by rememberSaveable { mutableStateOf(false) }
    val screenScrollState = rememberScrollState()
    val aboutScrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(screenScrollState)
            .padding(all = dimensionResource(R.dimen.spacing_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfileHeaderSection(uiState = uiState)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        ProfileStatsRow(
            guideLevel = uiState.guideLevel.ifBlank { stringResource(R.string.preview_super_guide) },
            rating = uiState.rating,
            tourCount = uiState.tourCount,
            onGuideLevelInfoClick = { },
        )
        Spacer(modifier = Modifier.height(24.dp))
        AboutSection(
            biography = uiState.biography,
            isAboutExpanded = isAboutExpanded,
            onExpandAboutClick = { isAboutExpanded = true },
            aboutScrollState = aboutScrollState,
        )
        Spacer(modifier = Modifier.height(24.dp))
        LanguagesSection(languages = uiState.spokenLanguages)
        Spacer(modifier = Modifier.height(24.dp))
        PopularToursSection(popularTours = uiState.popularTours)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
        MessageButtonSection()
    }
}

@Composable
private fun ProfileHeaderSection(
    uiState: GuideProfilePreviewUiState,
) {
    Box(
        modifier =
            Modifier
                .size(100.dp)
                .clip(CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = uiState.profileImageResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
    Text(
        text = uiState.displayName,
        color = colorResource(R.color.brand_color),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
    Text(
        text = uiState.title.ifBlank { stringResource(R.string.about_specialty_placeholder) },
        color = Color.Gray,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
private fun AboutSection(
    biography: String,
    isAboutExpanded: Boolean,
    onExpandAboutClick: () -> Unit,
    aboutScrollState: ScrollState,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.about),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small)),
        )
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(if (isAboutExpanded) 140.dp else 88.dp),
            shape = MaterialTheme.shapes.medium,
            color = Color.Transparent,
        ) {
            Text(
                text = biography.ifBlank { stringResource(R.string.preview_about_text) },
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_color),
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(aboutScrollState)
                        .padding(dimensionResource(R.dimen.spacing_small)),
            )
        }
        if (!isAboutExpanded) {
            Text(
                text = stringResource(R.string.preview_read_more),
                color = colorResource(R.color.brand_color),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                modifier =
                    Modifier
                        .clickable(onClick = onExpandAboutClick)
                        .padding(top = 4.dp, start = 8.dp),
            )
        }
    }
}

@Composable
private fun LanguagesSection(
    languages: List<GuideSpokenLanguageUi>,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.about_languages_title),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp),
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        ) {
            languages.forEach { language ->
                Surface(
                    shape = CircleShape,
                    color = colorResource(R.color.brand_color).copy(alpha = 0.12f),
                ) {
                    Text(
                        text = language.displayText,
                        color = colorResource(R.color.brand_color),
                        style = MaterialTheme.typography.labelMedium,
                        modifier =
                            Modifier.padding(
                                horizontal = dimensionResource(R.dimen.spacing_small),
                                vertical = dimensionResource(R.dimen.spacing_tiny),
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun PopularToursSection(
    popularTours: List<PopularTourCardUiModel>,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.preview_popular_tours_title),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small)),
        )
        Text(
            text = stringResource(R.string.preview_see_all),
            color = colorResource(R.color.brand_color),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
    }
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
    ) {
        popularTours.forEach { tour ->
            PopularTourCard(tour = tour)
        }
    }
}

@Composable
private fun MessageButtonSection() {
    EditButton(
        text = R.string.preview_send_message,
        onClick = { },
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_medium)),
    )
}
