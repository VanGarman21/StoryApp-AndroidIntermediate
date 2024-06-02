package com.dicoding.armand.storyapp.utils

import android.content.Context
import android.content.SharedPreferences
import hu.autsoft.krate.Krate
import hu.autsoft.krate.booleanPref
import hu.autsoft.krate.default.withDefault
import hu.autsoft.krate.stringPref

class UserPreference(context: Context) : Krate {
    override val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences("prefs_manager", Context.MODE_PRIVATE)

    var isEnabled by booleanPref().withDefault(false)
    var isExampleLogin by booleanPref().withDefault(false)
    var authToken by stringPref().withDefault("")

    fun clearPreferences() {
        sharedPreferences.edit().clear().apply()
    }

}