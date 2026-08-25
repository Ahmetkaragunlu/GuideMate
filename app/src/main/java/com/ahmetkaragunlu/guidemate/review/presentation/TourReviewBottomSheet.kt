package com.ahmetkaragunlu.guidemate.review.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditButton
import com.ahmetkaragunlu.guidemate.common.ui.components.EditTextField
import com.ahmetkaragunlu.guidemate.common.ui.components.RatingBar
import com.ahmetkaragunlu.guidemate.review.presentation.model.TourReviewFormUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourReviewBottomSheet(
    uiState: TourReviewFormUiState,
    onDismissRequest: () -> Unit,
    onRatingChanged: (Int) -> Unit,
    onCommentChanged: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    if (!uiState.isVisible) return

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .navigationBarsPadding()
                    .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                    .padding(bottom = dimensionResource(R.dimen.spacing_large)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
        ) {
            Text(
                text = stringResource(R.string.tour_review_title),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = stringResource(R.string.tour_review_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_color),
            )
            RatingBar(
                rating = uiState.rating,
                onRatingChanged = onRatingChanged,
                enabled = !uiState.isSubmitting,
            )
            uiState.errorMessage?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            EditTextField(
                value = uiState.comment,
                onValueChange = onCommentChanged,
                placeholder = R.string.tour_review_comment_placeholder,
                keyboardOptions =
                    KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                    ),
                singleLine = false,
                enabled = !uiState.isSubmitting,
                minLines = 4,
                maxLines = 6,
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.border_color),
                        unfocusedBorderColor = colorResource(R.color.border_color),
                    ),
                shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
                modifier = Modifier.fillMaxWidth(),
            )
            EditButton(
                text = R.string.submit_tour_review,
                onClick = onSubmit,
                enabled = !uiState.isSubmitting,
                isLoading = uiState.isSubmitting,
            )
        }
    }
}
