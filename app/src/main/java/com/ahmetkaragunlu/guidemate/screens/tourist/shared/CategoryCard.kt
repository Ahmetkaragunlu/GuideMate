package com.ahmetkaragunlu.guidemate.screens.tourist.shared


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R

@Composable
fun CategoryCard(
    category: CategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF4FC3F7) else MaterialTheme.colorScheme.onPrimary,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else colorResource(R.color.text_color)
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(category.icon, null)
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
            Text(
                text = stringResource(id = category.titleResId),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}