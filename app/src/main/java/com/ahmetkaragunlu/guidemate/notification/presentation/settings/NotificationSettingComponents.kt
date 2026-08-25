package com.ahmetkaragunlu.guidemate.notification.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R

@Composable
fun NotificationSettingsSectionTitle(title: String) {
    Text(
        text = title,
        color = colorResource(R.color.brand_color),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = 20.dp, top = 20.dp),
    )
}

@Composable
fun NotificationSettingsSwitchRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(end = dimensionResource(R.dimen.spacing_medium)),
        ) {
            Text(
                text = title,
                color = if (enabled) Color.Unspecified else colorResource(R.color.text_color),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = subtitle,
                color = colorResource(R.color.text_color),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 2.dp),
            )
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors =
                SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor = colorResource(R.color.brand_color),
                    uncheckedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    uncheckedTrackColor = Color(0xFFE0E0E0),
                    disabledCheckedTrackColor =
                        colorResource(R.color.brand_color).copy(alpha = 0.5f),
                    disabledCheckedThumbColor = Color(0xFFF5F5F5),
                ),
        )
    }
}
