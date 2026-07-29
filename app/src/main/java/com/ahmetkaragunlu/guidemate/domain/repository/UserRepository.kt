package com.ahmetkaragunlu.guidemate.domain.repository

import com.ahmetkaragunlu.guidemate.domain.model.UserState
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val userState: StateFlow<UserState>

    suspend fun restoreCachedUser(): UserState
}
