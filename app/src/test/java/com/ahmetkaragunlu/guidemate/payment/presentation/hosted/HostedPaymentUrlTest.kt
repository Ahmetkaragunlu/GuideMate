package com.ahmetkaragunlu.guidemate.payment.presentation.hosted

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
}
