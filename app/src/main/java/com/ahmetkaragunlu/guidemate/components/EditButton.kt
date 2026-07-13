package com.ahmetkaragunlu.guidemate.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R

@Composable
fun EditButton(
    @StringRes text: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    Button(
        onClick = { onClick() },
        enabled = enabled,
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.brand_color)),
        modifier =
            modifier
                .widthIn(max = 380.dp)
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_extra_large)),
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier =
                    Modifier
                        .padding(end = dimensionResource(R.dimen.spacing_tiny))
                        .size(18.dp),
            )
        }
        Text(
            text = stringResource(text),
        )
    }
}
