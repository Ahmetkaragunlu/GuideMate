package com.ahmetkaragunlu.guidemate.screens.tourist.home.model

import androidx.annotation.DrawableRes

data class BestGuideUiModel(
    val id: String,
    val name: String,
    @param:DrawableRes val imageResId: Int,
    val specialization: String,
    val rating: String,
)
