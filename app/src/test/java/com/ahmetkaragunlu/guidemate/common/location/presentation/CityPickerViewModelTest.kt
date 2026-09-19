package com.ahmetkaragunlu.guidemate.common.location.presentation

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.location.model.CityOption
import com.ahmetkaragunlu.guidemate.common.location.model.CitySearchResult
import com.ahmetkaragunlu.guidemate.common.location.search.CitySearchService
import com.ahmetkaragunlu.guidemate.common.location.search.CitySearchSession
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CityPickerViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun debounceCancelsPreviousQueryAndPublishesLatestResults() =
        runTest {
            val firstSearchStarted = CompletableDeferred<Unit>()
            val session =
                FakeCitySearchSession().apply {
                    searchHandler = { query ->
                        if (query == "is") {
                            firstSearchStarted.complete(Unit)
                            awaitCancellation()
                        }
                        Result.success(listOf(result(query)))
                    }
                }
            val viewModel = CityPickerViewModel(FakeCitySearchService(session))
            viewModel.start("TR")

            viewModel.onQueryChange("is")
            advanceTimeBy(349)
            runCurrent()
            assertTrue(session.searchQueries.isEmpty())

            advanceTimeBy(1)
            runCurrent()
            firstSearchStarted.await()

            viewModel.onQueryChange("ist")
            advanceTimeBy(350)
            runCurrent()

            assertEquals(listOf("is", "ist"), session.searchQueries)
            assertEquals("ist", viewModel.uiState.value.results.single().primaryText)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun shortQueryClearsResultsAndRetryRepeatsFailedNormalizedQuery() =
        runTest {
            val session = FakeCitySearchSession()
            val viewModel = CityPickerViewModel(FakeCitySearchService(session))
            viewModel.start("TR")

            session.searchResults += Result.success(listOf(result("Istanbul")))
            viewModel.onQueryChange("istanbul")
            advanceTimeBy(350)
            runCurrent()
            assertEquals(1, viewModel.uiState.value.results.size)

            viewModel.onQueryChange(" i ")
            assertTrue(viewModel.uiState.value.results.isEmpty())
            assertFalse(viewModel.uiState.value.hasError)

            session.searchResults += Result.failure(IllegalStateException("offline"))
            viewModel.onQueryChange("  izmir  ")
            advanceTimeBy(350)
            runCurrent()
            assertTrue(viewModel.uiState.value.hasError)
            assertEquals("izmir", session.searchQueries.last())

            session.searchResults += Result.success(listOf(result("Izmir")))
            viewModel.retry()
            runCurrent()

            assertFalse(viewModel.uiState.value.hasError)
            assertEquals("Izmir", viewModel.uiState.value.results.single().primaryText)
            assertEquals(listOf("istanbul", "izmir", "izmir"), session.searchQueries)
        }

    @Test
    fun resolvedSelectionCanBeConsumed() =
        runTest {
            val selectedCity = CityOption("place-1", "Istanbul", "TR")
            val session =
                FakeCitySearchSession().apply {
                    resolveResult = Result.success(selectedCity)
                }
            val viewModel = CityPickerViewModel(FakeCitySearchService(session))
            val result = result("Istanbul")
            viewModel.start("TR")

            viewModel.onResultSelected(result)
            runCurrent()

            assertEquals(result, session.resolvedResults.single())
            assertEquals(selectedCity, viewModel.uiState.value.selectedCity)
            assertFalse(viewModel.uiState.value.isResolvingSelection)

            viewModel.consumeSelection()

            assertNull(viewModel.uiState.value.selectedCity)
        }

    private fun result(name: String) =
        CitySearchResult(
            placeId = "place-$name",
            primaryText = name,
            secondaryText = "Turkiye",
        )

    private class FakeCitySearchService(
        private val session: CitySearchSession,
    ) : CitySearchService {
        override fun createSession(countryCode: String): CitySearchSession = session
    }

    private class FakeCitySearchSession : CitySearchSession {
        val searchQueries = mutableListOf<String>()
        val searchResults = ArrayDeque<Result<List<CitySearchResult>>>()
        val resolvedResults = mutableListOf<CitySearchResult>()
        var searchHandler: (suspend (String) -> Result<List<CitySearchResult>>)? = null
        var resolveResult: Result<CityOption> = Result.failure(IllegalStateException("Not configured"))

        override suspend fun search(query: String): Result<List<CitySearchResult>> {
            searchQueries += query
            return searchHandler?.invoke(query) ?: searchResults.removeFirst()
        }

        override suspend fun resolve(result: CitySearchResult): Result<CityOption> {
            resolvedResults += result
            return resolveResult
        }
    }
}
