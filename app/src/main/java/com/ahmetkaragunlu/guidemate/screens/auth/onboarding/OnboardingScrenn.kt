package com.ahmetkaragunlu.guidemate.screens.auth.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onboardingCompleted: () -> Unit,
) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { onboardingPages.size }
    )
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            state = pagerState,
        ) { pageIndex ->
            val page = onboardingPages[pageIndex]
            Column(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(page.image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_extra_large)))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                ) {
                    Text(
                        text = stringResource(page.title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF323539)
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
                    Text(
                        text = stringResource(page.description),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = colorResource(R.color.text_color)
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(36.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(onboardingPages.size) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) colorResource(R.color.brand_color) else Color.Gray
                val size = if (pagerState.currentPage == iteration) 12.dp else 8.dp
                Box(
                    modifier = Modifier
                        .padding(all = dimensionResource(R.dimen.spacing_tiny))
                        .size(size)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
        Spacer(modifier = Modifier.height(64.dp))
        EditButton(
            text = if (pagerState.currentPage == onboardingPages.size - 1) R.string.get_started else R.string.next,
            onClick = {
                scope.launch {
                    if (pagerState.currentPage < onboardingPages.size - 1) {
                        pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                    } else {
                        onboardingCompleted()
                    }
                }
            },
            )

    }
}