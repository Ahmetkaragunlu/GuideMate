package com.ahmetkaragunlu.guidemate.data.repository

import com.ahmetkaragunlu.guidemate.data.local.UserManager
import com.ahmetkaragunlu.guidemate.domain.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val userManager: UserManager,
    ) : UserRepository {
        override val userName: Flow<String?> = userManager.userName
        override val userLastName: Flow<String?> = userManager.userLastName

        override fun saveUser(
            firstName: String?,
            lastName: String?,
        ) {
            userManager.saveUser(firstName, lastName)
        }

        override fun clearUser() {
            userManager.clearUser()
        }
    }
