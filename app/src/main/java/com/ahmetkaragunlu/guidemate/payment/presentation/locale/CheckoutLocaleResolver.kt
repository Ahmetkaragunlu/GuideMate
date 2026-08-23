package com.ahmetkaragunlu.guidemate.payment.presentation.locale

import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutLocale
import java.util.Locale

fun currentCheckoutLocale(locale: Locale = Locale.getDefault()): CheckoutLocale =
    if (locale.language.equals("tr", ignoreCase = true)) {
        CheckoutLocale.TR
    } else {
        CheckoutLocale.EN
    }

