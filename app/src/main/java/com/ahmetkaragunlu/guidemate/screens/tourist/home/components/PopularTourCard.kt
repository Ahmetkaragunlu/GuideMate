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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.tourist.home.model.PopularToursCardUiModel

@Composable
fun PopularTourCard(
    tour: PopularToursCardUiModel
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(260.dp),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = tour.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.40f)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = tour.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
                    Text(
                        text = tour.rating,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.text_color)
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
                    Text(
                        text = tour.reviewCount,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(R.color.text_color)

                    )
                }
                Text(
                    text = tour.price,
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(R.color.brand_color),
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = tour.languagesFlag)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tour.languagesText,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = tour.guideImageUrl),
                        contentDescription = "Rehber",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.LightGray, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Rehber: ") }
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Normal,
                                    color = colorResource(R.color.text_color)
                                )
                            ) { append(tour.guideName) }
                        },
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
