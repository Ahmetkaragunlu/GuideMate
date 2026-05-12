package com.ahmetkaragunlu.guidemate.screens.guide.profile.guidelevel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.profile.guidelevel.model.GUIDE_LEVEL_DISPLAY_ORDER
import com.ahmetkaragunlu.guidemate.screens.guide.profile.guidelevel.model.GuideLevelTier
import com.ahmetkaragunlu.guidemate.screens.guide.profile.guidelevel.model.GuideLevelViewerType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideLevelInfoBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    currentLevel: GuideLevelTier,
    viewerType: GuideLevelViewerType,
    modifier: Modifier = Modifier,
) {
    if (!isVisible) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                    .padding(bottom = dimensionResource(R.dimen.spacing_medium)),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.guide_level_sheet_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.brand_color),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(R.string.guide_level_sheet_close_icon),
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(R.color.text_color),
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .clickable(onClick = onDismiss),
                )
            }

            Text(
                text = stringResource(R.string.guide_level_sheet_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_color),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))

            GUIDE_LEVEL_DISPLAY_ORDER.forEach { tier ->
                val isActive = tier == currentLevel
                val contentPadding =
                    if (tier == GuideLevelTier.SILVER) {
                        PaddingValues(14.dp)
                    } else {
                        PaddingValues(dimensionResource(R.dimen.spacing_medium))
                    }

                GuideLevelRowCard(
                    tier = tier,
                    isActive = isActive,
                    activeTagText = if (isActive) stringResource(viewerType.activeTagResId) else null,
                    contentPadding = contentPadding,
                )
            }

            Text(
                text = stringResource(R.string.guide_level_sheet_confirm),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_color),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .clickable(onClick = onDismiss),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun GuideLevelRowCard(
    tier: GuideLevelTier,
    isActive: Boolean,
    activeTagText: String?,
    contentPadding: PaddingValues,
) {
    Surface(
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        color = if (isActive) Color(0xFFF8FBFF) else MaterialTheme.colorScheme.onPrimary,
        border =
            BorderStroke(
                width = 1.dp,
                color = if (isActive) Color(0xFFB9D6FF) else Color(0xFFE8EAF1),
            ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(tier.iconResId),
                style = MaterialTheme.typography.titleLarge,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(if (isActive) 6.dp else 2.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))) {
                    Text(
                        text = stringResource(tier.titleResId),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.SemiBold,
                    )
                    if (isActive && activeTagText != null) {
                        Text(
                            text = activeTagText,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF1B8D2E),
                            modifier =
                                Modifier
                                    .background(
                                        color = Color(0xFFE8F7EC),
                                        shape = RoundedCornerShape(10.dp),
                                    )
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                        )
                    }
                }
                Text(
                    text = stringResource(tier.descriptionResId),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.text_color),
                )
            }
        }
    }
}
