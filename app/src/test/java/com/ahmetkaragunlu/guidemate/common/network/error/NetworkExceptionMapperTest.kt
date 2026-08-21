package com.ahmetkaragunlu.guidemate.common.network.error

import com.ahmetkaragunlu.guidemate.common.result.AppError
import java.io.IOException
import org.junit.Assert.assertSame
import org.junit.Test

class NetworkExceptionMapperTest {
    private val mapper = NetworkExceptionMapper()

    @Test
    fun `preserves structured error carried by token refresh failure`() {
        val exception = TestNetworkErrorException(AppError.SessionExpired)

        assertSame(AppError.SessionExpired, mapper.map(exception))
    }

    private class TestNetworkErrorException(
        override val error: AppError,
    ) : IOException(), NetworkErrorCarrier
}
