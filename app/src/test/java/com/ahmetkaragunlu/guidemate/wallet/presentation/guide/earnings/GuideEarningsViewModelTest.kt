package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.testing.FakeGuideFinanceRepository
import com.ahmetkaragunlu.guidemate.testing.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.testNotification
import com.ahmetkaragunlu.guidemate.wallet.domain.model.MonthlyGuideEarning
import java.time.YearMonth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GuideEarningsViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun pendingMonthlyEarningIsExposedToTheUi() =
        runTest {
            val currentPeriod = YearMonth.now()
            val repository =
                FakeGuideFinanceRepository().apply {
                    monthlyEarningsResult =
                        DataResult.Success(
                            listOf(
                                MonthlyGuideEarning(
                                    year = currentPeriod.year,
                                    month = currentPeriod.monthValue,
                                    netEarningsMinor = 45_000,
                                    currencyCode = "USD",
                                    pendingEarningsMinor = 12_000,
                                ),
                            ),
                        )
                }

            val viewModel =
                GuideEarningsViewModel(
                    repository,
                    FakeNotificationRepository(),
                    FakeResourceProvider(),
                )
            runCurrent()

            val currentMonth = viewModel.uiState.value.currentMonth
            assertEquals(45_000L, currentMonth?.amountMinor)
            assertEquals(12_000L, currentMonth?.pendingEarningsMinor)
        }

    @Test
    fun earningAvailableNotificationRefreshesCurrentYearWithoutChangingSelection() =
        runTest {
            val currentYear = YearMonth.now().year
            val previousYear = currentYear - 1
            val repository = FakeGuideFinanceRepository()
            val notificationRepository = FakeNotificationRepository()
            val viewModel =
                GuideEarningsViewModel(
                    repository,
                    notificationRepository,
                    FakeResourceProvider(),
                )
            runCurrent()

            viewModel.selectYear(previousYear)
            runCurrent()
            assertEquals(previousYear, viewModel.uiState.value.selectedYear)

            notificationRepository.notificationState.value = listOf(testNotification())
            runCurrent()

            assertEquals(
                listOf(currentYear, previousYear, currentYear),
                repository.requestedMonthlyEarningsYears,
            )
            assertEquals(previousYear, viewModel.uiState.value.selectedYear)
        }
}
