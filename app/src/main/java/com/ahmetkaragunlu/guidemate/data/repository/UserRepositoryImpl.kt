package com.ahmetkaragunlu.guidemate.data.repository

import com.ahmetkaragunlu.guidemate.data.local.preferences.AppPreferencesDataSource
import com.ahmetkaragunlu.guidemate.domain.model.UserState
import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val preferences: AppPreferencesDataSource,
) : UserRepository {
    override val userState: StateFlow<UserState> = preferences.userState

    override suspend fun restoreCachedUser(): UserState = preferences.restoreUser()
}
