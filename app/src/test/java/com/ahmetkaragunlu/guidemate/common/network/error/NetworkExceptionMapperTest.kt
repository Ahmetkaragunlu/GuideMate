package com.ahmetkaragunlu.guidemate.common.network.error

import com.ahmetkaragunlu.guidemate.common.result.AppError
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class NetworkExceptionMapperTest {
    private val mapper = NetworkExceptionMapper()

    @Test
    fun `preserves structured error carried by token refresh failure`() {
        val exception = TestNetworkErrorException(AppError.SessionExpired)

        assertSame(AppError.SessionExpired, mapper.map(exception))
    }

    @Test
    fun `maps unavailable network exceptions to their user facing categories`() {
        assertSame(AppError.NoInternet, mapper.map(UnknownHostException()))
        assertSame(AppError.NoInternet, mapper.map(IOException()))
        assertSame(AppError.NoResponseFromServer, mapper.map(ConnectException()))
        assertSame(AppError.NoResponseFromServer, mapper.map(SocketTimeoutException()))
    }

    @Test
    fun `maps http status and unknown exceptions without losing status code`() {
        val httpException =
            HttpException(Response.error<Unit>(503, "unavailable".toResponseBody()))

        assertEquals(AppError.Server(503), mapper.map(httpException))
        assertSame(AppError.Unknown, mapper.map(IllegalStateException()))
    }

    private class TestNetworkErrorException(
        override val error: AppError,
    ) : IOException(), NetworkErrorCarrier
}
