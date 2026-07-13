package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.components

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun GuideTourPublishValidationMessage(
    @StringRes errorResId: Int?,
    modifier: Modifier = Modifier,
) {
    errorResId?.let { messageResId ->
        Text(
            text = stringResource(messageResId),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = modifier,
        )
    }
}
