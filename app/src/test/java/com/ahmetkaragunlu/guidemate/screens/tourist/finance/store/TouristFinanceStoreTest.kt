package com.ahmetkaragunlu.guidemate.screens.tourist.finance.store

import org.junit.Assert.assertEquals
import org.junit.Test

class TouristFinanceStoreTest {
    @Test
    fun `making card default updates shared finance state and ordering`() {
        val store = TouristFinanceStore()

        store.makeDefaultCard("tourist-card-2")

        assertEquals("tourist-card-2", store.state.value.defaultCard?.cardId)
        assertEquals("tourist-card-2", store.state.value.savedCards.first().cardId)
    }
}
