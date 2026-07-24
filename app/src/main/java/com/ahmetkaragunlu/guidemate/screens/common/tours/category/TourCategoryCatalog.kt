package com.ahmetkaragunlu.guidemate.screens.common.tours.category

import com.ahmetkaragunlu.guidemate.R
import compose.icons.TablerIcons
import compose.icons.tablericons.BuildingChurch
import compose.icons.tablericons.Confetti
import compose.icons.tablericons.Flame
import compose.icons.tablericons.LayoutGrid
import compose.icons.tablericons.Palette
import compose.icons.tablericons.ToolsKitchen
import compose.icons.tablericons.Trees

object TourCategoryCatalog {
    val categories = TourCategory.entries.map(::uiModelFor)

    val filterOptions =
        listOf(
            TourCategoryUiModel(
                category = null,
                titleResId = R.string.category_all,
                icon = TablerIcons.LayoutGrid,
            ),
        ) + categories

    fun uiModelFor(category: TourCategory): TourCategoryUiModel =
        when (category) {
            TourCategory.CULTURE ->
                TourCategoryUiModel(
                    category = TourCategory.CULTURE,
                    titleResId = R.string.category_culture,
                    icon = TablerIcons.BuildingChurch,
                )

            TourCategory.FOOD ->
                TourCategoryUiModel(
                    category = TourCategory.FOOD,
                    titleResId = R.string.category_food,
                    icon = TablerIcons.ToolsKitchen,
                )

            TourCategory.NATURE ->
                TourCategoryUiModel(
                    category = TourCategory.NATURE,
                    titleResId = R.string.category_nature,
                    icon = TablerIcons.Trees,
                )

            TourCategory.ART ->
                TourCategoryUiModel(
                    category = TourCategory.ART,
                    titleResId = R.string.category_art,
                    icon = TablerIcons.Palette,
                )

            TourCategory.ENTERTAINMENT ->
                TourCategoryUiModel(
                    category = TourCategory.ENTERTAINMENT,
                    titleResId = R.string.category_entertainment,
                    icon = TablerIcons.Confetti,
                )

            TourCategory.ADVENTURE ->
                TourCategoryUiModel(
                    category = TourCategory.ADVENTURE,
                    titleResId = R.string.category_adventure,
                    icon = TablerIcons.Flame,
                )
        }
}
