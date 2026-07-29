package com.ahmetkaragunlu.guidemate.screens.auth.roleselection

import com.ahmetkaragunlu.guidemate.domain.model.UserRole

data class RoleSelectionScreenState(
    val selectedRole: UserRole? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
