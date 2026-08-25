package com.ahmetkaragunlu.guidemate.discovery.presentation.tourist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditTextField
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.profile.presentation.components.GuideResultCard
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideResultUiModel

@Composable
internal fun GuideExploreResults(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    guides: List<GuideResultUiModel>,
    loadState: ContentLoadState,
    isLoadingMore: Boolean,
    appendFailed: Boolean,
    canLoadMore: Boolean,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onGuideClick: (Long) -> Unit,
) {
    Column(modifier = modifier.padding(dimensionResource(R.dimen.spacing_medium))) {
        EditTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.brand_color),
                    unfocusedBorderColor = colorResource(R.color.brand_color),
                    cursorColor = colorResource(R.color.brand_color),
                ),
            placeholder = R.string.search_guide,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null, tint = Color.Gray)
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        GuideMateContentState(
            state = loadState,
            onRetry = onRetry,
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) {
            if (guides.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.guide_search_empty),
                        color = colorResource(R.color.text_color),
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(guides, key = { it.guideId }) { guide ->
                        GuideResultCard(
                            guide = guide,
                            onClick = { onGuideClick(guide.guideId) },
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
                    }
                    when {
                        isLoadingMore ->
                            item {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(dimensionResource(R.dimen.spacing_medium)),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    CircularProgressIndicator(color = colorResource(R.color.brand_color))
                                }
                            }
                        appendFailed ->
                            item {
                                GuideMateContentState(
                                    state = ContentLoadState.ERROR,
                                    onRetry = onLoadMore,
                                    modifier = Modifier.fillMaxWidth().height(112.dp),
                                ) {}
                            }
                        canLoadMore ->
                            item {
                                LaunchedEffect(guides.size) {
                                    onLoadMore()
                                }
                            }
                    }
                }
            }
        }
    }
}
