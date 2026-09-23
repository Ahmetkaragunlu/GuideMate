package com.ahmetkaragunlu.guidemate.common.network

import com.ahmetkaragunlu.guidemate.review.data.remote.api.ReviewApi
import com.ahmetkaragunlu.guidemate.tour.data.remote.api.GuideTourApi
import com.ahmetkaragunlu.guidemate.wallet.data.remote.api.GuideFinanceApi
import java.lang.reflect.Method
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

class GuideOwnedApiPathContractTest {
    @Test
    fun `guide tour api uses canonical guides me paths`() {
        assertEquals(
            setOf(
                "api/v1/guides/me/tours",
                "api/v1/guides/me/tours/{tourId}",
                "api/v1/guides/me/tours/{tourId}/change-requests",
                "api/v1/guides/me/tours/{tourId}/sessions",
                "api/v1/guides/me/sessions/{sessionId}",
                "api/v1/guides/me/sessions/{sessionId}/open",
                "api/v1/guides/me/sessions/{sessionId}/close",
                "api/v1/guides/me/sessions/{sessionId}/cancel",
                "api/v1/guides/me/tours/{tourId}/archive",
                "api/v1/guides/me/dashboard",
            ),
            endpointPaths(GuideTourApi::class.java),
        )
    }

    @Test
    fun `guide finance api uses canonical guides me paths`() {
        assertEquals(
            setOf(
                "api/v1/guides/me/earnings",
                "api/v1/guides/me/earnings/monthly",
                "api/v1/guides/me/bank-accounts",
                "api/v1/guides/me/bank-accounts/{bankAccountId}/default",
                "api/v1/guides/me/bank-accounts/{bankAccountId}",
                "api/v1/guides/me/withdrawals",
            ),
            endpointPaths(GuideFinanceApi::class.java),
        )
    }

    @Test
    fun `review api keeps public paths and uses canonical owned path`() {
        assertEquals(
            setOf(
                "api/v1/reservations/{reservationId}/reviews",
                "api/v1/tours/{tourId}/reviews",
                "api/v1/guides/me/tours/{tourId}/reviews",
            ),
            endpointPaths(ReviewApi::class.java),
        )
    }

    private fun endpointPaths(api: Class<*>): Set<String> =
        api.declaredMethods.mapNotNull(::endpointPath).toSet()

    private fun endpointPath(method: Method): String? =
        method.getAnnotation(GET::class.java)?.value
            ?: method.getAnnotation(POST::class.java)?.value
            ?: method.getAnnotation(PATCH::class.java)?.value
            ?: method.getAnnotation(DELETE::class.java)?.value
}
