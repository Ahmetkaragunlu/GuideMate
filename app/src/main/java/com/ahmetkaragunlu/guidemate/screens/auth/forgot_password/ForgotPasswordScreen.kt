package com.ahmetkaragunlu.guidemate.screens.auth.forgot_password

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditTextField

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onNavigateToSignIn: () -> Unit
) {
    val context = LocalContext.current
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(screenState.errorMessage) {
        screenState.errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    if (screenState.showSuccessDialog) {
        EditAlertDialog(
            title = R.string.success,
            text = R.string.reset_password_link_sent,
            onDismissRequest = {
                viewModel.dismissSuccessDialog()
                onNavigateToSignIn()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissSuccessDialog()
                        onNavigateToSignIn()
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                top = dimensionResource(R.dimen.spacing_double_extra_large),
                start = dimensionResource(R.dimen.spacing_medium),
                end = dimensionResource(R.dimen.spacing_medium)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.reset_password_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
        Text(
            text = stringResource(R.string.reset_password_description),
            color = colorResource(R.color.text_color),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        EditTextField(
            value = formState.firstName,
            onValueChange = { viewModel.onFirstNameChange(it) },
            placeholder = R.string.name,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        EditTextField(
            value = formState.lastName,
            onValueChange = { viewModel.onLastNameChange(it) },
            placeholder = R.string.last_name,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        EditTextField(
            value = formState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            placeholder = R.string.email,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

        EditButton(
            text = R.string.send_reset_link,
            onClick = {
                viewModel.onSubmitClick { errorResId ->
                    Toast.makeText(context, context.getString(errorResId), Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}