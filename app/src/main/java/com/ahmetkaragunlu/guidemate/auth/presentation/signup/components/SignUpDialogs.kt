package com.ahmetkaragunlu.guidemate.auth.presentation.signup.components

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditAlertDialog

@Composable
internal fun RegistrationSuccessDialog(onDismiss: () -> Unit) {
    EditAlertDialog(
        title = R.string.verification_required_title,
        text = R.string.registration_success_message,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.brand_color)),
            ) {
                Text(stringResource(R.string.ok))
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TermsBottomSheet(
    sheetState: SheetState,
    hasUserReadTerms: Boolean,
    onDismiss: () -> Unit,
    onMarkTermsAsRead: () -> Unit,
    onAcceptTerms: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        val scrollState = rememberScrollState()
        val isCurrentlyAtBottom by remember {
            derivedStateOf {
                scrollState.maxValue == 0 || scrollState.value >= scrollState.maxValue - 50
            }
        }
        LaunchedEffect(isCurrentlyAtBottom) {
            if (isCurrentlyAtBottom) onMarkTermsAsRead()
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = dimensionResource(R.dimen.spacing_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.terms_dialog_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
            Column(
                modifier = Modifier.weight(1f, fill = false).verticalScroll(scrollState),
            ) {
                Text(
                    text = stringResource(R.string.terms_and_conditions_full_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onAcceptTerms,
                modifier = Modifier.fillMaxWidth(),
                enabled = hasUserReadTerms,
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
                            if (hasUserReadTerms) {
                                R.string.terms_read_and_approve
                            } else {
                                R.string.terms_continue_reading
                            },
                        ),
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
