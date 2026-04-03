package com.ahmetkaragunlu.guidemate.data.repository

import com.ahmetkaragunlu.guidemate.data.local.UserManager
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.domain.model.UserState
import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userManager: UserManager,
) : UserRepository {

    override val userState: StateFlow<UserState> = userManager.userState

    override fun saveUser(firstName: String?, lastName: String?, role: UserRole?) {
        userManager.saveUser(firstName, lastName, role)
    }

    override fun saveUserRole(role: UserRole) {
        userManager.saveUserRole(role)
    }

    override fun clearUser() {
        userManager.clearUser()
    }
}
