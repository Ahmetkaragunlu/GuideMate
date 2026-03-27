package com.ahmetkaragunlu.guidemate.domain.repository

import com.ahmetkaragunlu.guidemate.domain.model.UserState
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val userState: StateFlow<UserState>

    fun saveUser(
        firstName: String?,
        lastName: String?,
        role: String?
    )

    fun saveUserRole(role: String)

    fun clearUser()
}