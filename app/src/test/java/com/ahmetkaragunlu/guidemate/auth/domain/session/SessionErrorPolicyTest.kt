package com.ahmetkaragunlu.guidemate.auth.domain.session

import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SessionErrorPolicyTest {
    @Test
    fun `terminal authentication errors require local session cleanup`() {
        val terminalCodes =
            listOf(
                BackendErrorCode.ACCOUNT_DISABLED,
                BackendErrorCode.INVALID_REFRESH_TOKEN,
                BackendErrorCode.REFRESH_TOKEN_EXPIRED,
                BackendErrorCode.REFRESH_TOKEN_REPLAY,
                BackendErrorCode.UNAUTHORIZED,
            )

        terminalCodes.forEach { code ->
            assertTrue(AppError.Backend(code, fallbackMessage = null).isTerminalSessionError())
        }
        assertTrue(AppError.SessionExpired.isTerminalSessionError())
    }

    @Test
    fun `temporary network and validation errors keep the session`() {
        assertFalse(AppError.NoInternet.isTerminalSessionError())
        assertFalse(
            AppError.Backend(BackendErrorCode.VALIDATION_FAILED, fallbackMessage = null)
                .isTerminalSessionError(),
        )
    }
}
