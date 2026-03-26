package com.ahmetkaragunlu.guidemate.screens.guide.tours.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R

@Composable
fun TourBaseCard(
    imageUrl: Int,
    modifier: Modifier = Modifier,
    colorFilter: ColorFilter? = null,
    alpha: Float = 1f,
    elevation: Dp = 4.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier =
            modifier
                .widthIn(max = 720.dp)
                .fillMaxWidth()
                .alpha(alpha),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = colorFilter,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp),
            )
            content()
        }
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    text: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorResource(R.color.text_color),
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.text_color),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
