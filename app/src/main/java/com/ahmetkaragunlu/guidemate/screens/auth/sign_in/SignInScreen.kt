package com.ahmetkaragunlu.guidemate.screens.auth.sign_in


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditTextField

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
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
        EditTextField(
            value = "",
            onValueChange = {},
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
            }
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        EditTextField(
            value = "",
            onValueChange = {},
            placeholder = R.string.password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
        Row(
            modifier = Modifier
                .widthIn(max = 380.dp)
                .fillMaxWidth()
                .padding(horizontal = 56.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = stringResource(R.string.forgot_password),
                modifier = Modifier.clickable {

                },
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.onboarding_body_text_color)
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        EditButton(
            text = R.string.login,
            onClick = {}
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
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
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
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
        TextButton(
            onClick = { },
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_large))
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
                            color = colorResource(R.color.brand_color),
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



