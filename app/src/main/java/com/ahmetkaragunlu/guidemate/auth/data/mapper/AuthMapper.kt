package com.ahmetkaragunlu.guidemate.auth.data.mapper

import com.ahmetkaragunlu.guidemate.auth.data.remote.model.RoleType
import com.ahmetkaragunlu.guidemate.auth.data.remote.model.response.AuthResponse
import com.ahmetkaragunlu.guidemate.auth.data.remote.model.response.CurrentUserResponse
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState

internal fun AuthResponse.toDomain(): UserState =
    UserState(
        userId = userId,
        email = email,
        firstName = firstName,
        lastName = lastName,
        isRoleSelected = isRoleSelected,
        role = role?.toDomain(),
        avatarMediaId = avatar?.mediaAssetId,
        avatarUrl = avatar?.imageUrl,
    )

internal fun CurrentUserResponse.toDomain(): UserState =
    UserState(
        userId = userId,
        email = email,
        firstName = firstName,
        lastName = lastName,
        isRoleSelected = isRoleSelected,
        role = role?.toDomain(),
        avatarMediaId = avatar?.mediaAssetId,
        avatarUrl = avatar?.imageUrl,
    )

internal fun UserRole.toNetwork(): RoleType =
    when (this) {
        UserRole.TOURIST -> RoleType.ROLE_TOURIST
        UserRole.GUIDE -> RoleType.ROLE_GUIDE
    }

private fun RoleType.toDomain(): UserRole =
    when (this) {
        RoleType.ROLE_TOURIST -> UserRole.TOURIST
        RoleType.ROLE_GUIDE -> UserRole.GUIDE
    }
