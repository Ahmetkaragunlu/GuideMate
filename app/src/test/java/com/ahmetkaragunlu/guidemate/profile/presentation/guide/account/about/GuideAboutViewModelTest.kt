package com.ahmetkaragunlu.guidemate.profile.presentation.guide.account.about

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.testing.profile.FakeGuideProfileRepository
import com.ahmetkaragunlu.guidemate.testing.common.FakeResourceProvider
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

            assertEquals("City Historian", repository.calls.update?.specialtyTitle)
            assertEquals(
                "A sufficiently detailed biography for visitors.",
                repository.calls.update?.biography,
            )
            assertEquals(listOf("tr"), repository.calls.update?.languageCodes)
            assertTrue(viewModel.uiState.value.saveCompleted)
        }
}
