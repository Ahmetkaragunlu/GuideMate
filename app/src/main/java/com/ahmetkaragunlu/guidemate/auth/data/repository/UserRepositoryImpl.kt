package com.ahmetkaragunlu.guidemate.auth.data.repository

import com.ahmetkaragunlu.guidemate.auth.data.local.preferences.AuthPreferencesDataSource
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val preferences: AuthPreferencesDataSource,
) : UserRepository {
    override val userState: StateFlow<UserState> = preferences.userState

    override suspend fun restoreCachedUser(): UserState = preferences.restoreUser()

    override suspend fun updateAvatar(mediaAssetId: String, imageUrl: String) {
        preferences.saveUser(
            userState.value.copy(
                avatarMediaId = mediaAssetId,
                avatarUrl = imageUrl,
            ),
        )
    }
}
