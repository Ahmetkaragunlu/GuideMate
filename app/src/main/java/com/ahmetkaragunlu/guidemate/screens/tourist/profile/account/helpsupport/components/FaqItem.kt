package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.helpsupport.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ahmetkaragunlu.guidemate.R

@Composable
fun FaqItem(
    @StringRes titleResId: Int,
    @StringRes descriptionResId: Int,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.spacing_small)
        ),
    ) {
        Text(
            text = stringResource(titleResId),
            color = Color.Unspecified,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = stringResource(descriptionResId),
            color = colorResource(R.color.text_color),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
