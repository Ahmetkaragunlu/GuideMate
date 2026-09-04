package com.ahmetkaragunlu.guidemate.payment.presentation.locale

import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutLocale
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckoutLocaleResolverTest {
    @Test
    fun `hosted checkout locale is Turkish`() {
        assertEquals(CheckoutLocale.TR, currentCheckoutLocale())
    }
}
