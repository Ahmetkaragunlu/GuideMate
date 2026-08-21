package com.ahmetkaragunlu.guidemate.auth.presentation.roleselection

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditButton
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RoleSelectionScreen(
    modifier: Modifier = Modifier,
    viewModel: RoleSelectionViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(screenState.errorMessage) {
        screenState.errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = MaterialTheme.colorScheme.onPrimary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.choose_user_type_title),
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_small)),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.choose_user_type_description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = colorResource(R.color.text_color),
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_double_extra_large)))
        RoleSelectionCard(
            icon = R.drawable.traveler_image,
            title = R.string.traveler,
            description = R.string.traveler_description,
            selected = screenState.selectedRole == UserRole.TOURIST,
            onClick = { viewModel.selectRole(UserRole.TOURIST) },
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        RoleSelectionCard(
            icon = R.drawable.guide_image,
            title = R.string.local_guide,
            description = R.string.local_guide_description,
            selected = screenState.selectedRole == UserRole.GUIDE,
            onClick = { viewModel.selectRole(UserRole.GUIDE) },
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_extra_large)))
        EditButton(
            text = R.string.next,
            onClick = viewModel::confirmRoleSelection,
            enabled = screenState.selectedRole != null,
            isLoading = screenState.isLoading,
        )
    }
}

@Composable
private fun RoleSelectionCard(
    @DrawableRes icon: Int,
    @StringRes title: Int,
    @StringRes description: Int,
    selected: Boolean,
    onClick: () -> Unit,
) {
    OutlinedCard(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        border = BorderStroke(width = 1.dp, color = Color(0XFFdfe2e9)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(all = dimensionResource(R.dimen.spacing_medium)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Color.Unspecified,
            )
            Column(
                modifier =
                    Modifier
                        .padding(all = dimensionResource(R.dimen.spacing_medium))
                        .weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_tiny)),
                )
                Text(
                    text = stringResource(description),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.text_color),
                )
            }
            RadioButton(
                selected = selected,
                onClick = onClick,
            )
        }
    }
}
