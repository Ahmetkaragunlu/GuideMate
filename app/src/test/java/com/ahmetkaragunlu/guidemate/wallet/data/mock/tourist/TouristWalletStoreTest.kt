package com.ahmetkaragunlu.guidemate.wallet.data.mock.tourist

import org.junit.Assert.assertEquals
import org.junit.Test

class TouristWalletStoreTest {
    @Test
    fun `making card default updates shared wallet state and ordering`() {
        val store = TouristWalletStore()

        store.makeDefaultCard("tourist-card-2")

        assertEquals("tourist-card-2", store.state.value.defaultCard?.cardId)
        assertEquals("tourist-card-2", store.state.value.savedCards.first().cardId)
    }
}
