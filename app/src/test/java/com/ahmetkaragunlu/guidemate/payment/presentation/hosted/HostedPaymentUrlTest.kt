package com.ahmetkaragunlu.guidemate.payment.presentation.hosted

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HostedPaymentUrlTest {
    @Test
    fun `accepts only secure hosted payment urls`() {
        assertTrue("https://sandbox.iyzipay.com/checkout".isSecureHostedPaymentUrl())
        assertFalse("http://sandbox.iyzipay.com/checkout".isSecureHostedPaymentUrl())
        assertFalse("https:checkout".isSecureHostedPaymentUrl())
        assertFalse("file:///tmp/payment.html".isSecureHostedPaymentUrl())
        assertFalse("not-a-url".isSecureHostedPaymentUrl())
    }

    @Test
    fun `recognizes only the payment callback path`() {
        assertTrue(
            "https://local.example/api/v1/payments/iyzico/callback?token=abc"
                .isPaymentCallbackUrl(),
        )
        assertTrue("https://local.example/api/v1/payments/iyzico/callback/".isPaymentCallbackUrl())
        assertFalse("https://local.example/api/v1/payments/iyzico/callback/other".isPaymentCallbackUrl())
        assertFalse("https://sandbox.iyzipay.com/checkout".isPaymentCallbackUrl())
    }

    @Test
    fun `web history is consumed before payment cancellation`() {
        assertEquals(
            HostedPaymentBackAction.NAVIGATE_WEB_VIEW_BACK,
            resolveHostedPaymentBackAction(
                isVerifyingCallback = false,
                canWebViewGoBack = true,
            ),
        )
        assertEquals(
            HostedPaymentBackAction.CONFIRM_CANCELLATION,
            resolveHostedPaymentBackAction(
                isVerifyingCallback = false,
                canWebViewGoBack = false,
            ),
        )
        assertEquals(
            HostedPaymentBackAction.IGNORE,
            resolveHostedPaymentBackAction(
                isVerifyingCallback = true,
                canWebViewGoBack = true,
            ),
        )
    }
}
