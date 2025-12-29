package com.ahmetkaragunlu.guidemate.screens.auth

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton

@Composable
fun RoleSelectionScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.onPrimary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.choose_user_type_title),
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.choose_user_type_description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = colorResource(R.color.onboarding_body_text_color)
        )
        Spacer(modifier = Modifier.height(72.dp))
        RoleSelectionCard(
            icon = R.drawable.traveler_image,
            title = R.string.traveler,
            description = R.string.traveler_description
        )
        Spacer(modifier = Modifier.height(16.dp))
        RoleSelectionCard(
            icon = R.drawable.guide_image,
            title = R.string.local_guide,
            description = R.string.local_guide_description
        )
        Spacer(modifier = Modifier.height(48.dp))
        EditButton(
            text = R.string.next,
            onClick = {}
        )

    }
}


@Composable
fun RoleSelectionCard(
    @DrawableRes icon: Int,
    @StringRes title: Int,
    @StringRes description: Int,
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        border = BorderStroke(width = 1.dp, color = Color(0XFFdfe2e9)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Column(modifier = Modifier.padding( 16.dp).weight(1f),verticalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = stringResource(description),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.onboarding_body_text_color)

                    )
            }
           RadioButton( selected = false, onClick = {})

        }
    }
}

