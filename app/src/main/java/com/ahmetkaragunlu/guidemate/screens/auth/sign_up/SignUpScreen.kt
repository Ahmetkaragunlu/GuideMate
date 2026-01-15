package com.ahmetkaragunlu.guidemate.screens.auth.sign_up


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditTextField
import com.ahmetkaragunlu.guidemate.screens.auth.auth_viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(
                bottom = dimensionResource(R.dimen.spacing_large),
                top = dimensionResource(R.dimen.spacing_double_extra_large)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.sign_up_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = modifier.height(dimensionResource(R.dimen.spacing_small)))
        Text(
            text = stringResource(R.string.sign_up_subtitle),
            color = colorResource(R.color.onboarding_body_text_color),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_extra_large)))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            EditTextField(
                value = viewModel.inputFirstName,
                onValueChange = { viewModel.inputFirstNameChange(it) },
                placeholder = R.string.name,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                isError = viewModel.shouldShowFirstNameError(),
                supportingText = if (viewModel.shouldShowFirstNameError()) R.string.name_error_message else null
            )
            EditTextField(
                value = viewModel.inputLastName,
                onValueChange = { viewModel.onLastNameChange(it) },
                placeholder = R.string.last_name,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                isError = viewModel.shouldShowLastNameError(),
                supportingText = if (viewModel.shouldShowLastNameError()) R.string.last_name_error_message else null
            )
            EditTextField(
                value = viewModel.inputEmail,
                onValueChange = { viewModel.onEmailChange(it) },
                placeholder = R.string.email,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = viewModel.shouldShowEmailError(),
                supportingText = if (viewModel.shouldShowEmailError()) R.string.email_error_message else null
            )
            EditTextField(
                value = viewModel.inputPassword,
                onValueChange = { viewModel.onPasswordChange(it) },
                placeholder = R.string.password,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = if (viewModel.passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                isError = viewModel.shouldShowPasswordError(),
                trailingIcon = {
                    Icon(
                        imageVector = if (viewModel.passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.clickable {
                            viewModel.passwordVisibility = !viewModel.passwordVisibility
                        }
                    )
                },
                supportingText = if (viewModel.shouldShowPasswordError()) R.string.password_error_message else null,
            )
            EditTextField(
                value = viewModel.inputConfirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChange(it) },
                placeholder = R.string.confirm_password,
                visualTransformation = if (viewModel.confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    Icon(
                        imageVector = if (viewModel.confirmPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.clickable {
                            viewModel.confirmPasswordVisibility =
                                !viewModel.confirmPasswordVisibility
                        }
                    )
                },
                isError = viewModel.shouldShowConfirmPasswordError(),
                supportingText = if (viewModel.shouldShowConfirmPasswordError()) R.string.confirm_password_error_message else null
            )

        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
        Row(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_extra_large)),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Checkbox(
                checked = false,
                onCheckedChange = {},
                colors = CheckboxDefaults.colors(uncheckedColor = colorResource(R.color.brand_color))
            )
            Text(
                text = stringResource(R.string.agree_terms_conditions),
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.onboarding_body_text_color),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        EditButton(
            text = R.string.sign_up,
            onClick = {

            }
        )
        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = { }
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = colorResource(R.color.onboarding_body_text_color)
                        )
                    ) {
                        append(stringResource(R.string.already_have_an_account))
                        append("  ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = colorResource(R.color.brand_color),
                        )
                    ) {
                        append(stringResource(R.string.login))
                    }
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

}
