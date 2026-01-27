package com.ahmetkaragunlu.guidemate.screens.tourist.home.components



import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.tourist.home.model.BestGuideUiModel

@Composable
fun BestGuideCard(
    guide: BestGuideUiModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = guide.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = guide.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
                Text(
                    text = guide.specialization,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp
                )
            }
            Surface(
                color = Color(0xFFFF9F1C),
                shape = RoundedCornerShape(dimensionResource(R.dimen.radius_small)),
                modifier = Modifier.wrapContentSize()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
                    Text(
                        text = guide.rating,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}