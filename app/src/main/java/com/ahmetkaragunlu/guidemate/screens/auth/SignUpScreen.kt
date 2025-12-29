package com.ahmetkaragunlu.guidemate.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditTextField

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier

) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 72.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.sign_up_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                text = stringResource(R.string.sign_up_subtitle),
                color = colorResource(R.color.onboarding_body_text_color),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(48.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EditTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = R.string.name,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),

                    )
                EditTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = R.string.last_name,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                )
                EditTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = R.string.email,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                )
                EditTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = R.string.password,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                )
                EditTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = R.string.confirm_password,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 36.dp, top = 6.dp),
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Checkbox(
                    checked = false,
                    onCheckedChange = {},
                    colors = CheckboxDefaults.colors(uncheckedColor = Color.Gray)
                )
                Text(
                    text = stringResource(R.string.agree_terms_conditions),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.onboarding_body_text_color),
                    modifier= Modifier.padding(top = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            EditButton(
                text = R.string.sign_up,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(48.dp))

            TextButton(
                onClick = {}
            ) {
                Text(
                    text = stringResource(R.string.already_have_an_account),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.onboarding_body_text_color)
                )
            }

        }


    }
}
