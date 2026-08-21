package com.ahmetkaragunlu.guidemate.wallet.data.mock.guide

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GuideWalletStoreTest {
    @Test
    fun `making bank account default updates shared wallet state and ordering`() {
        val store = GuideWalletStore()

        store.makeDefaultBankAccount("guide-bank-account-2")

        assertEquals("guide-bank-account-2", store.state.value.defaultBankAccount?.bankAccountId)
        assertEquals("guide-bank-account-2", store.state.value.bankAccounts.first().bankAccountId)
    }

    @Test
    fun `pending withdrawal retains target bank account and reserves available balance`() {
        val store = GuideWalletStore()

        val created =
            store.addPendingWithdrawal(
                amountMinor = 500_000,
                bankAccountId = "guide-bank-account-2",
            )

        assertTrue(created)
        assertEquals(
            "guide-bank-account-2",
            store.state.value.recentTransactions.first().bankAccountId,
        )
        assertEquals(1_500_000L, store.state.value.availableWithdrawalBalanceMinor)
    }
}
