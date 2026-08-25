package com.ahmetkaragunlu.guidemate.common.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import compose.icons.TablerIcons
import compose.icons.tablericons.Refresh

@Composable
fun GuideMateContentState(
    state: ContentLoadState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    content: @Composable () -> Unit,
) {
    when (state) {
        ContentLoadState.LOADING -> RequestStateContent(isLoading = true, modifier = modifier)
        ContentLoadState.ERROR ->
            RequestStateContent(
                isLoading = false,
                onRetry = onRetry,
                errorMessage = errorMessage,
                modifier = modifier,
            )
        ContentLoadState.CONTENT -> content()
    }
}

@Composable
private fun RequestStateContent(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
    errorMessage: String? = null,
) {
    val brandColor = colorResource(R.color.brand_color)
    val indicatorSize = dimensionResource(R.dimen.request_state_indicator_size)
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                dimensionResource(R.dimen.spacing_small),
                alignment = Alignment.CenterVertically,
            ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(indicatorSize),
                color = brandColor,
            )
        } else {
            IconButton(onClick = onRetry) {
                Icon(
                    imageVector = TablerIcons.Refresh,
                    contentDescription = stringResource(R.string.common_retry),
                    tint = brandColor,
                    modifier = Modifier.size(indicatorSize),
                )
            }
        }
        if (!isLoading && errorMessage != null) {
            Text(
                text = errorMessage,
                color = colorResource(R.color.text_color),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Text(
            text =
                stringResource(
                    if (isLoading) R.string.common_loading else R.string.common_retry,
                ),
            color = colorResource(R.color.text_color),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable(enabled = !isLoading, onClick = onRetry),
        )
    }
}
