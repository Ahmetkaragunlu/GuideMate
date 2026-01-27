package com.ahmetkaragunlu.guidemate.screens.tourist.shared

import com.ahmetkaragunlu.guidemate.R
import compose.icons.TablerIcons
import compose.icons.tablericons.*


object TourCategoriesData {
    val categories = listOf(
        CategoryItem(TourCategory.ALL, R.string.category_all, TablerIcons.LayoutGrid),
        CategoryItem(TourCategory.CULTURE, R.string.category_culture, TablerIcons.BuildingChurch),
        CategoryItem(TourCategory.FOOD, R.string.category_food, TablerIcons.ToolsKitchen),
        CategoryItem(TourCategory.NATURE, R.string.category_nature, TablerIcons.Trees),
        CategoryItem(TourCategory.ART, R.string.category_art, TablerIcons.Palette),
        CategoryItem(TourCategory.ENTERTAINMENT, R.string.category_entertainment, TablerIcons.Confetti),
        CategoryItem(TourCategory.ADVENTURE, R.string.category_adventure, TablerIcons.Flame)
    )
}