package com.ahmetkaragunlu.guidemate.domain

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userName: Flow<String?>
    val userLastName: Flow<String?>

    fun saveUser(
        firstName: String?,
        lastName: String?,
    )

    fun clearUser()
}
