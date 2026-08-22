package com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSavedCardBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                    .padding(bottom = dimensionResource(R.dimen.spacing_large)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = colorResource(R.color.brand_color),
                modifier =
                    Modifier
                        .size(48.dp)
                        .background(
                            color = colorResource(R.color.brand_color).copy(alpha = 0.10f),
                            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
                        )
                        .padding(dimensionResource(R.dimen.spacing_small)),
            )

            Text(
                text = stringResource(R.string.add_new_card),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            Text(
                text = stringResource(R.string.secure_card_redirect_description),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_color),
                textAlign = TextAlign.Center,
            )

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFF4F7FC),
                            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
                        )
                        .padding(dimensionResource(R.dimen.spacing_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
            ) {
                Text(
                    text = stringResource(R.string.secure_card_provider_notice),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.brand_color),
                )
                Text(
                    text = stringResource(R.string.secure_card_storage_notice),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.text_color),
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))

            EditButton(
                text = R.string.ok,
                onClick = onConfirm,
            )
        }
    }
}
