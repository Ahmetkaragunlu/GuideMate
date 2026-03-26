package com.ahmetkaragunlu.guidemate.screens.guide.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronRight

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = dimensionResource(R.dimen.spacing_medium)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_medium)))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.text_color),
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = TablerIcons.ChevronRight,
            contentDescription = null,
            tint = Color.Gray,
        )
    }
}
