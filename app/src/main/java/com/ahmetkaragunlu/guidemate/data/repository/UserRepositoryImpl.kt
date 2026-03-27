package com.ahmetkaragunlu.guidemate.data.repository

import com.ahmetkaragunlu.guidemate.data.local.UserManager
import com.ahmetkaragunlu.guidemate.data.local.UserState
import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userManager: UserManager,
) : UserRepository {

    override val userState: StateFlow<UserState> = userManager.userState

    override fun saveUser(
        firstName: String?,
        lastName: String?,
        role: String?
    ) {
        userManager.saveUser(firstName, lastName, role)
    }

    override fun saveUserRole(role: String) {
        userManager.saveUserRole(role)
    }

    override fun clearUser() {
        userManager.clearUser()
    }
}