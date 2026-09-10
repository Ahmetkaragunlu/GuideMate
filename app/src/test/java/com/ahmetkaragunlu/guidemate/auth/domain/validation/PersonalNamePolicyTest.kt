package com.ahmetkaragunlu.guidemate.auth.domain.validation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PersonalNamePolicyTest {
    private val policy = PersonalNamePolicy()

    @Test
    fun `international names and supported separators are valid`() {
        listOf("José", "Anne-Marie", "O'Connor", "D'Arcy", "İlker Can").forEach { name ->
            assertTrue(name, policy.isValidFirstName(name))
        }
    }

    @Test
    fun `first name minimum counts letters rather than separators`() {
        listOf("aa ", "a a", "a-", "a''a", "Anne  Marie").forEach { name ->
            assertFalse(name, policy.isValidFirstName(name))
        }
    }

    @Test
    fun `last name accepts two letters and normalizes surrounding spaces`() {
        assertTrue(policy.isValidLastName(" Li "))
        assertTrue(policy.isValidLastName("O'Connor"))
        assertFalse(policy.isValidLastName("A "))
    }
}
