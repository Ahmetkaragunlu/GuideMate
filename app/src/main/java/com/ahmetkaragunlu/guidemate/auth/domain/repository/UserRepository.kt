package com.ahmetkaragunlu.guidemate.auth.domain.repository

import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val userState: StateFlow<UserState>

    suspend fun restoreCachedUser(): UserState

    suspend fun updateAvatar(mediaAssetId: String, imageUrl: String)
}
