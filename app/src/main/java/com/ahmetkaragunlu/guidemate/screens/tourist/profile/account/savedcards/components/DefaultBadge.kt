package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R

@Composable
fun DefaultBadge() {
    Text(
        text = stringResource(R.string.default_card),
        modifier = Modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.radius_medium)))
            .background(colorResource(id = R.color.brand_color).copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = colorResource(id = R.color.brand_color),
    )
}

