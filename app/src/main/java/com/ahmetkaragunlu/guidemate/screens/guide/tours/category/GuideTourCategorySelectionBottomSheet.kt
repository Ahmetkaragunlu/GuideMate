package com.ahmetkaragunlu.guidemate.screens.guide.tours.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategory
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategoryCatalog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideTourCategorySelectionBottomSheet(
    isVisible: Boolean,
    selectedCategory: TourCategory?,
    onDismissRequest: () -> Unit,
    onCategorySelected: (TourCategory) -> Unit,
) {
    if (!isVisible) return

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                    .padding(bottom = dimensionResource(R.dimen.spacing_medium)),
        ) {
            Text(
                text = stringResource(R.string.guide_tour_publish_step2_category_label),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(max = 460.dp)
                        .padding(top = dimensionResource(R.dimen.spacing_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
            ) {
                items(
                    items = TourCategoryCatalog.categories,
                    key = { checkNotNull(it.category).code },
                ) { item ->
                    val category = checkNotNull(item.category)
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCategorySelected(category)
                                    onDismissRequest()
                                }
                                .padding(vertical = dimensionResource(R.dimen.spacing_small)),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .size(36.dp)
                                    .background(
                                        color =
                                            if (category == selectedCategory) {
                                                colorResource(R.color.category_selected_color)
                                            } else {
                                                colorResource(R.color.text_color).copy(alpha = 0.08f)
                                            },
                                        shape = CircleShape,
                                    ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint =
                                    if (category == selectedCategory) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        colorResource(R.color.text_color)
                                    },
                            )
                        }
                        Text(
                            text = stringResource(item.titleResId),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f),
                        )
                        if (category == selectedCategory) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = colorResource(R.color.category_selected_color),
                            )
                        }
                    }
                }
            }
        }
    }
}
