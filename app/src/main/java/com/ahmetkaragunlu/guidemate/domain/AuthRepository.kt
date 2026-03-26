package com.ahmetkaragunlu.guidemate.domain

import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.domain.model.AuthResult
import com.ahmetkaragunlu.guidemate.domain.model.UserRole

interface AuthRepository {
    suspend fun login(email: String, password: String): DataResult<AuthResult>
    suspend fun register(firstName: String, lastName: String, email: String, password: String): DataResult<String>
    suspend fun googleLogin(idToken: String): DataResult<AuthResult>
    suspend fun logout(): DataResult<String>
    suspend fun refreshToken(): DataResult<AuthResult>
    suspend fun selectRole(role: UserRole): DataResult<AuthResult>
    suspend fun forgotPassword(email: String, firstName: String, lastName: String): DataResult<String>
    suspend fun resetPassword(token: String, newPassword: String, confirmPassword: String): DataResult<String>
}