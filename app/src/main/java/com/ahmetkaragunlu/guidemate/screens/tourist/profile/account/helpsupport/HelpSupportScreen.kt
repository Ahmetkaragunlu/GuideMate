package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.helpsupport

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
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.helpsupport.components.ContactSupportSection
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.helpsupport.components.FaqItem
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.helpsupport.model.faqEntries

@Composable
fun HelpSupportScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large)),
    ) {
        SupportHeader()
        SupportFaqSection()
        ContactSupportSection(onContactSupportClick = { })
    }
}

@Composable
private fun SupportHeader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.support_intro),
            color = colorResource(R.color.text_color),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun SupportFaqSection() {
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
