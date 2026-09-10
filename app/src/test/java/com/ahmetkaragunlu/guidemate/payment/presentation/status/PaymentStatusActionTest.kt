package com.ahmetkaragunlu.guidemate.payment.presentation.status

import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentUiStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class PaymentStatusActionTest {
    @Test
    fun `timeout retries status without exiting and failed payment exits`() {
        var retryCalls = 0
        var exitCalls = 0

        handlePaymentStatusPrimaryAction(
            status = PaymentUiStatus.TIMEOUT,
            onRetry = { retryCalls++ },
            onExit = { exitCalls++ },
        )

        assertEquals(1, retryCalls)
        assertEquals(0, exitCalls)

        handlePaymentStatusPrimaryAction(
            status = PaymentUiStatus.FAILED,
            onRetry = { retryCalls++ },
            onExit = { exitCalls++ },
        )

        assertEquals(1, retryCalls)
        assertEquals(1, exitCalls)
    }
}
