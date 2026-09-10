package com.ahmetkaragunlu.guidemate.profile.presentation.publicprofile

import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatConversation
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessage
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatMessageHistory
import com.ahmetkaragunlu.guidemate.chat.domain.repository.ChatRepository
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.FakeGuideProfileRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.FakeReviewRepository
import com.ahmetkaragunlu.guidemate.testing.FakeTourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.testing.testTourSearchItem
import com.ahmetkaragunlu.guidemate.testing.tourSearchPage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GuidePublicProfileViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun failedTourPreviewKeepsProfileVisibleAndRetryLoadsOnlyTours() =
        runTest(mainDispatcherRule.dispatcher) {
            val profileRepository = FakeGuideProfileRepository()
            val tourRepository =
                FakeTourDiscoveryRepository().apply {
                    popularForGuideResults += DataResult.Error(AppError.NoInternet)
                    popularForGuideResults +=
                        DataResult.Success(
                            tourSearchPage(
                                page = 0,
                                isLast = true,
                                testTourSearchItem("tour-1", "session-1"),
                            )
                        )
                }
            val viewModel =
                GuidePublicProfileViewModel(
                    profileRepository = profileRepository,
                    tourRepository = tourRepository,
                    reviewRepository = FakeReviewRepository(),
                    chatRepository = UnusedChatRepository(),
                    resourceProvider = FakeResourceProvider(),
                )

            viewModel.loadGuide(7)
            advanceUntilIdle()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertEquals(ContentLoadState.ERROR, viewModel.uiState.value.popularToursLoadState)
            assertEquals("Ada Guide", viewModel.uiState.value.displayName)
            assertTrue(viewModel.uiState.value.popularTours.isEmpty())

            viewModel.retryPopularTours()
            advanceUntilIdle()

            assertEquals(listOf(7L), profileRepository.publicProfileRequests)
            assertEquals(2, tourRepository.popularForGuideRequests.size)
            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.popularToursLoadState)
            assertEquals("session-1", viewModel.uiState.value.popularTours.single().id)
        }

    private class UnusedChatRepository : ChatRepository {
        override val conversations: StateFlow<List<ChatConversation>> = MutableStateFlow(emptyList())
        override val totalUnreadCount: StateFlow<Int> = MutableStateFlow(0)

        override fun observeMessages(chatId: String): Flow<ChatMessageHistory> = emptyFlow()

        override suspend fun refreshConversations(): DataResult<List<ChatConversation>> =
            error("Not used")

        override suspend fun refreshUnreadCount(): DataResult<Int> = error("Not used")

        override suspend fun loadInitialMessages(chatId: String): DataResult<ChatMessageHistory> =
            error("Not used")

        override suspend fun loadOlderMessages(chatId: String): DataResult<ChatMessageHistory> =
            error("Not used")

        override suspend fun sendMessage(chatId: String, text: String): DataResult<ChatMessage> =
            error("Not used")

        override suspend fun retryMessage(
            chatId: String,
            clientMessageId: String,
        ): DataResult<ChatMessage> = error("Not used")

        override suspend fun markRead(chatId: String): DataResult<Int> = error("Not used")

        override suspend fun clearConversation(chatId: String): DataResult<Int> = error("Not used")

        override suspend fun findOrCreate(remoteUserId: Long): DataResult<ChatConversation> =
            error("Not used")
    }
}
