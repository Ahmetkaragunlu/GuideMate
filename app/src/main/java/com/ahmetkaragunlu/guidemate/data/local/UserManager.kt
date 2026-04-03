package com.ahmetkaragunlu.guidemate.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.domain.model.UserState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    @ApplicationContext context: Context,
) {
    private companion object {
        const val ROLE_TOURIST = "ROLE_TOURIST"
        const val ROLE_GUIDE = "ROLE_GUIDE"
        const val ROLE_ADMIN = "ROLE_ADMIN"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val _userState = MutableStateFlow(
        UserState(
            firstName = prefs.getString("first_name", null),
            lastName = prefs.getString("last_name", null),
            role = prefs.getString("user_role", null)?.toUserRoleOrNull(),
        ),
    )

    val userState: StateFlow<UserState> = _userState.asStateFlow()

    fun saveUser(firstName: String?, lastName: String?, role: UserRole?) {
        prefs.edit {
            putString("first_name", firstName)
            putString("last_name", lastName)
            putString("user_role", role?.toStoredValue())
        }
        _userState.value = UserState(firstName, lastName, role)
    }

    fun saveUserRole(role: UserRole) {
        prefs.edit { putString("user_role", role.toStoredValue()) }
        _userState.value = _userState.value.copy(role = role)
    }

    fun clearUser() {
        prefs.edit {
            remove("first_name")
            remove("last_name")
            remove("user_role")
        }
        _userState.value = UserState(null, null, null)
    }

    private fun String.toUserRoleOrNull(): UserRole? =
        when (this) {
            ROLE_TOURIST,
            UserRole.TOURIST.name,
            -> UserRole.TOURIST

            ROLE_GUIDE,
            UserRole.GUIDE.name,
            -> UserRole.GUIDE

            ROLE_ADMIN,
            UserRole.ADMIN.name,
            -> UserRole.ADMIN

            else -> null
        }

    private fun UserRole.toStoredValue(): String =
        when (this) {
            UserRole.TOURIST -> ROLE_TOURIST
            UserRole.GUIDE -> ROLE_GUIDE
            UserRole.ADMIN -> ROLE_ADMIN
        }
}
