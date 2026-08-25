package com.ahmetkaragunlu.guidemate.auth.presentation.changepassword.model

data class ChangePasswordFormState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val currentPasswordVisible: Boolean = false,
    val newPasswordVisible: Boolean = false,
    val confirmNewPasswordVisible: Boolean = false,
)
