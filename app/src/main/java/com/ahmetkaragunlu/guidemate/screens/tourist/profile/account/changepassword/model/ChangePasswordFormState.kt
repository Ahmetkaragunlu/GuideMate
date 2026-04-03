package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.changepassword.model

data class ChangePasswordFormState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val currentPasswordVisible: Boolean = false,
    val newPasswordVisible: Boolean = false,
    val confirmNewPasswordVisible: Boolean = false,
) {
    val isCurrentPasswordValid: Boolean
        get() = currentPassword.isValidPassword()

    val isNewPasswordValid: Boolean
        get() = newPassword.isValidPassword()

    val isConfirmNewPasswordValid: Boolean
        get() = newPassword == confirmNewPassword

    private fun String.isValidPassword(): Boolean =
        length >= MIN_PASSWORD_LENGTH && all { it.isDigit() }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }
}
