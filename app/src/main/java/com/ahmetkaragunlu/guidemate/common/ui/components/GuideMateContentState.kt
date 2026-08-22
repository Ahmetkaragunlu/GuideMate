package com.ahmetkaragunlu.guidemate.common.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

@Composable
fun GuideMateContentState(
    state: ContentLoadState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    when (state) {
        ContentLoadState.LOADING -> RequestStateContent(isLoading = true, modifier = modifier)
        ContentLoadState.ERROR ->
            RequestStateContent(
                isLoading = false,
                onRetry = onRetry,
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
) {
    val brandColor = colorResource(R.color.brand_color)
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .clickable(
                    enabled = !isLoading,
                    role = Role.Button,
                    onClick = onRetry,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                dimensionResource(R.dimen.spacing_small),
                alignment = Alignment.CenterVertically,
            ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = brandColor)
        } else {
            Canvas(modifier = Modifier.size(48.dp)) {
                drawCircle(
                    color = brandColor,
                    radius = size.minDimension / 2f - 2.dp.toPx(),
                    center = Offset(size.width / 2f, size.height / 2f),
                    style = Stroke(width = 4.dp.toPx()),
                )
            }
        }
        Text(
            text =
                stringResource(
                    if (isLoading) R.string.common_loading else R.string.common_retry,
                ),
            color = colorResource(R.color.text_color),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
