package com.ahmetkaragunlu.guidemate.profile.presentation.guide.account.about

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.testing.FakeGuideProfileRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class GuideAboutViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun validEditedProfileIsTrimmedAndSavedWithLanguageCodes() =
        runTest {
            val repository = FakeGuideProfileRepository()
            val viewModel = GuideAboutViewModel(repository, FakeResourceProvider())
            runCurrent()

            viewModel.onSpecialtyTitleChange("  City Historian  ")
            viewModel.onBiographyChange("  A sufficiently detailed biography for visitors.  ")
            viewModel.onLanguagesSelected(listOf(LanguageOption("tr", "Turkce", "")))
            viewModel.onSaveClick()
            runCurrent()

            assertEquals("City Historian", repository.lastUpdate?.specialtyTitle)
            assertEquals(
                "A sufficiently detailed biography for visitors.",
                repository.lastUpdate?.biography,
            )
            assertEquals(listOf("tr"), repository.lastUpdate?.languageCodes)
            assertTrue(viewModel.uiState.value.saveCompleted)
        }
}
