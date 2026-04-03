package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.legalagreements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.legalagreements.components.LegalClauseItem
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.legalagreements.model.legalClauses

@Composable
fun LegalAgreementsScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        LegalHeader()
        LegalClausesSection()
        LegalVersionFooter()
    }
}

@Composable
private fun LegalHeader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.legal_title),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            text = stringResource(R.string.legal_intro),
            color = colorResource(R.color.text_color),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun LegalClausesSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        legalClauses.forEach { clause ->
            LegalClauseItem(
                titleResId = clause.titleResId,
                descriptionResId = clause.descriptionResId,
            )
        }
    }
}

@Composable
private fun LegalVersionFooter() {
    Column {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.app_version),
            color = colorResource(R.color.text_color),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
