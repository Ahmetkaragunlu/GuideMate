package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.testing.wallet.FakeGuideFinanceRepository
import com.ahmetkaragunlu.guidemate.testing.notification.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.testing.common.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.notification.testNotification
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
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
                    results.monthlyEarnings =
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
            viewModel.refresh()
            runCurrent()

            val currentMonth = viewModel.uiState.value.currentMonth
            assertEquals(45_000L, currentMonth?.amountMinor)
            assertEquals(12_000L, currentMonth?.pendingEarningsMinor)
        }

    @Test
    fun guideEarningNotificationsRefreshCurrentYearWithoutChangingSelection() =
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

            notificationRepository.state.notifications.value =
                listOf(testNotification(id = "purchase", type = NotificationType.TOUR_PURCHASED))
            runCurrent()

            assertEquals(
                listOf(previousYear, currentYear),
                repository.calls.requestedMonthlyEarningsYears,
            )
            assertEquals(previousYear, viewModel.uiState.value.selectedYear)

            notificationRepository.state.notifications.value =
                listOf(
                    testNotification(id = "earning", type = NotificationType.EARNING_AVAILABLE),
                    testNotification(id = "purchase", type = NotificationType.TOUR_PURCHASED),
                )
            runCurrent()

            assertEquals(
                listOf(previousYear, currentYear, currentYear),
                repository.calls.requestedMonthlyEarningsYears,
            )
            assertEquals(previousYear, viewModel.uiState.value.selectedYear)
        }
}
