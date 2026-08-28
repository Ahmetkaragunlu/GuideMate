package com.ahmetkaragunlu.guidemate.auth.data.mapper

import com.ahmetkaragunlu.guidemate.auth.data.remote.model.RoleType
import com.ahmetkaragunlu.guidemate.auth.data.remote.model.response.AuthResponse
import com.ahmetkaragunlu.guidemate.auth.data.remote.model.response.CurrentUserResponse
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaReferenceResponseDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthMapperTest {
    @Test
    fun `auth response preserves identity and selected role`() {
        val result =
            AuthResponse(
                    accessToken = "access",
                    refreshToken = "refresh",
                    message = null,
                    userId = 42,
                    email = "guide@example.com",
                    firstName = "Ada",
                    lastName = "Lovelace",
                    isRoleSelected = true,
                    role = RoleType.ROLE_GUIDE,
                    avatar = MediaReferenceResponseDto("avatar-1", "https://example.com/avatar.jpg"),
                )
                .toDomain()

        assertEquals(42L, result.userId)
        assertEquals("guide@example.com", result.email)
        assertEquals("Ada", result.firstName)
        assertEquals("Lovelace", result.lastName)
        assertTrue(result.isRoleSelected)
        assertEquals(UserRole.GUIDE, result.role)
        assertEquals("avatar-1", result.avatarMediaId)
        assertEquals("https://example.com/avatar.jpg", result.avatarUrl)
    }

    @Test
    fun `current user keeps unselected role nullable`() {
        val result =
            CurrentUserResponse(
                    userId = 7,
                    email = "tourist@example.com",
                    firstName = "Grace",
                    lastName = "Hopper",
                    isRoleSelected = false,
                    role = null,
                    avatar = null,
                )
                .toDomain()

        assertEquals(7L, result.userId)
        assertEquals(null, result.role)
        assertEquals(UserRole.TOURIST.toNetwork(), RoleType.ROLE_TOURIST)
    }
}
