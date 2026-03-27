package com.ahmetkaragunlu.guidemate.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val _userState = MutableStateFlow(
        UserState(
            firstName = prefs.getString("first_name", null),
            lastName = prefs.getString("last_name", null),
            role = prefs.getString("user_role", null)
        )
    )

    val userState: StateFlow<UserState> = _userState.asStateFlow()

    fun saveUser(
        firstName: String?,
        lastName: String?,
        role: String?
    ) {
        prefs.edit {
            putString("first_name", firstName)
            putString("last_name", lastName)
            putString("user_role", role)
        }
        _userState.value = UserState(firstName, lastName, role)
    }

    fun saveUserRole(role: String) {
        prefs.edit {
            putString("user_role", role)
        }
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
}