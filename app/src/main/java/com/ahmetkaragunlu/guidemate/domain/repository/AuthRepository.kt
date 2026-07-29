package com.ahmetkaragunlu.guidemate.domain.repository

import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.domain.model.UserState

interface AuthRepository {
    suspend fun login(email: String, password: String): DataResult<UserState>
    suspend fun register(firstName: String, lastName: String, email: String, password: String): DataResult<Unit>
    suspend fun googleLogin(idToken: String): DataResult<UserState>
    suspend fun logout(): DataResult<Unit>
    suspend fun selectRole(role: UserRole): DataResult<UserState>
    suspend fun currentUser(): DataResult<UserState>
    suspend fun resendVerification(email: String): DataResult<Unit>
    suspend fun forgotPassword(email: String): DataResult<Unit>
    suspend fun changePassword(currentPassword: String, newPassword: String): DataResult<Unit>
    fun hasStoredSession(): Boolean
    suspend fun clearLocalSession()
}
