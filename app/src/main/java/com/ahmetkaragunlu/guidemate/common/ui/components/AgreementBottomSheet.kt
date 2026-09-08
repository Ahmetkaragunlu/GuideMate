package com.ahmetkaragunlu.guidemate.common.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ahmetkaragunlu.guidemate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgreementBottomSheet(
    sheetState: SheetState,
    @StringRes titleResId: Int,
    @StringRes bodyResId: Int,
    hasUserReadAgreement: Boolean,
    onDismiss: () -> Unit,
    onMarkAgreementAsRead: () -> Unit,
    onAcceptAgreement: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        val scrollState = rememberScrollState()
        var hasMeasuredScrollContainer by remember { mutableStateOf(false) }
        val isAtBottom by remember {
            derivedStateOf {
                hasMeasuredScrollContainer && !scrollState.canScrollForward
            }
        }
        LaunchedEffect(isAtBottom) {
            if (isAtBottom) onMarkAgreementAsRead()
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(R.dimen.spacing_large),
                        vertical = dimensionResource(R.dimen.spacing_medium),
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(titleResId),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
            Column(
                modifier =
                    Modifier
                        .weight(1f, fill = false)
                        .onGloballyPositioned { hasMeasuredScrollContainer = true }
                        .verticalScroll(scrollState),
            ) {
                Text(
                    text = stringResource(bodyResId),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
            Button(
                onClick = onAcceptAgreement,
                modifier = Modifier.fillMaxWidth(),
                enabled = hasUserReadAgreement,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.brand_color),
                        disabledContainerColor = Color.Gray,
                        contentColor = Color.White,
                        disabledContentColor = Color.White,
                    ),
            ) {
                Text(
                    text =
                        stringResource(
                            if (hasUserReadAgreement) {
                                R.string.agreement_read_and_approve
                            } else {
                                R.string.agreement_continue_reading
                            },
                        ),
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
        }
    }
}
