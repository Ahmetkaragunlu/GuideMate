package com.ahmetkaragunlu.guidemate.screens.auth.sign_in

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditTextField
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToRoleSelection: () -> Unit
) {
    val context = LocalContext.current
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    // Google Sign-In Launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account.idToken?.let { token ->
                viewModel.onGoogleSignInSuccess(token)
            } ?: run {
                Toast.makeText(
                    context,
                    context.getString(R.string.google_sign_in_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: ApiException) {
            Toast.makeText(
                context,
                context.getString(R.string.google_sign_in_error, e.message ?: "Unknown error"),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Error Message Toast
    LaunchedEffect(screenState.errorMessage) {
        screenState.errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    // Navigation Handler
    LaunchedEffect(screenState.navigateToRoleSelection) {
        if (screenState.navigateToRoleSelection) {
            onNavigateToRoleSelection()
            viewModel.resetNavigationState()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                top = dimensionResource(R.dimen.spacing_double_extra_large),
                bottom = dimensionResource(R.dimen.spacing_large)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.sign_in_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
        Text(
            text = stringResource(R.string.sign_in_subtitle),
            color = colorResource(R.color.onboarding_body_text_color),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_double_extra_large)))

        // Email Field
        EditTextField(
            value = formState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            placeholder = R.string.email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            isError = !viewModel.isValidEmail() && formState.email.isNotEmpty(),
            supportingText = if (!viewModel.isValidEmail() && formState.email.isNotEmpty())
                R.string.email_error_message else null
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        // Password Field
        EditTextField(
            value = formState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            placeholder = R.string.password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (formState.passwordVisibility)
                VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = if (formState.passwordVisibility)
                        Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.clickable { viewModel.togglePasswordVisibility() }
                )
            },
            isError = !viewModel.isValidPassword() && formState.password.isNotEmpty(),
            supportingText = if (!viewModel.isValidPassword() && formState.password.isNotEmpty())
                R.string.password_error_message else null
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))

        // Forgot Password Link
        Row(
            modifier = Modifier
                .widthIn(max = 380.dp)
                .fillMaxWidth()
                .padding(horizontal = 56.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = stringResource(R.string.forgot_password),
                modifier = Modifier.clickable { onNavigateToForgotPassword() },
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.onboarding_body_text_color)
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        // Login Button
        EditButton(
            text = R.string.login,
            onClick = {
                viewModel.onSignInClick { errorResId ->
                    Toast.makeText(context, context.getString(errorResId), Toast.LENGTH_SHORT).show()
                }
            }
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

        // Divider with "OR"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color.Gray.copy(alpha = 0.4f),
                thickness = 1.dp
            )
            Text(
                text = stringResource(R.string.or_continue_with),
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_small)),
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.onboarding_body_text_color)
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color.Gray.copy(alpha = 0.4f),
                thickness = 1.dp
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_extra_large)))

        // Google Sign-In Button
        Button(
            onClick = {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                googleSignInClient.signOut().addOnCompleteListener {
                    googleSignInLauncher.launch(googleSignInClient.signInIntent)
                }
                      },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary
            ),
            border = BorderStroke(width = 1.dp, color = Color.LightGray),
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_extra_large)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large))
        ) {
            Icon(
                painter = painterResource(R.drawable.google_icon),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
            Text(
                text = stringResource(R.string.google),
                style = MaterialTheme.typography.labelLarge,
                color = colorResource(R.color.onboarding_body_text_color).copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Sign Up Link
        TextButton(
            onClick = { onNavigateToSignUp() }
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = colorResource(R.color.onboarding_body_text_color)
                        )
                    ) {
                        append(stringResource(R.string.dont_have_an_account))
                        append("  ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = colorResource(R.color.brand_color)
                        )
                    ) {
                        append(stringResource(R.string.sign_up_text))
                    }
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}