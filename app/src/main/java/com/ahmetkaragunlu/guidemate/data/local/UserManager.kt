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
class UserManager
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) {
        private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        private val _userName = MutableStateFlow(prefs.getString("first_name", null))
        val userName: StateFlow<String?> = _userName.asStateFlow()

        private val _userLastName = MutableStateFlow(prefs.getString("last_name", null))
        val userLastName: StateFlow<String?> = _userLastName.asStateFlow()

        fun saveUser(
            firstName: String?,
            lastName: String?,
        ) {
            prefs.edit {
                putString("first_name", firstName)
                putString("last_name", lastName)
            }
            _userName.value = firstName
            _userLastName.value = lastName
        }

        fun clearUser() {
            prefs.edit {
                remove("first_name")
                remove("last_name")
            }
            _userName.value = null
            _userLastName.value = null
        }
    }
