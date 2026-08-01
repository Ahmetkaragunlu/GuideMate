package com.ahmetkaragunlu.guidemate.screens.common.helpsupport

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.helpsupport.components.ContactSupportSection
import com.ahmetkaragunlu.guidemate.screens.common.helpsupport.components.FaqItem
import com.ahmetkaragunlu.guidemate.screens.common.helpsupport.model.FaqEntry

@Composable
fun HelpSupportScreen(
    @StringRes introResId: Int,
    faqEntries: List<FaqEntry>,
    modifier: Modifier = Modifier,
    onContactSupportClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large)),
    ) {
        SupportHeader(introResId = introResId)
        SupportFaqSection(faqEntries = faqEntries)
        ContactSupportSection(onContactSupportClick = onContactSupportClick)
    }
}

@Composable
private fun SupportHeader(
    @StringRes introResId: Int,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(introResId),
            color = colorResource(R.color.text_color),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun SupportFaqSection(faqEntries: List<FaqEntry>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        faqEntries.forEach { faq ->
            FaqItem(
                titleResId = faq.titleResId,
                descriptionResId = faq.descriptionResId,
            )
        }
    }
}
