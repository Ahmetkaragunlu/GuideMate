package com.ahmetkaragunlu.guidemate.screens.auth.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R

data class OnboardingPage(
    @StringRes val title: Int,
    @StringRes val description: Int,
    @DrawableRes val image: Int
)


val onboardingPages = listOf(
   OnboardingPage(
       title = R.string.onboarding_title_1,
       description = R.string.onboarding_description_1,
       image = R.drawable.onboarding_tourist
   ),
    OnboardingPage(
        title = R.string.onboarding_title_2,
        description = R.string.onboarding_description2,
        image = R.drawable.onboarding_guide
    ),
    OnboardingPage(
        title = R.string.onboarding_title_3,
        description = R.string.onboarding_description_3,
        image = R.drawable.onboarding_deal
    )
)

