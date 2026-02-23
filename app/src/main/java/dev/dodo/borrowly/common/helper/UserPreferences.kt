package dev.dodo.borrowly.common.helper

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "user_prefs")

object PrefKey {
    val HAS_ONBOARDED = booleanPreferencesKey("has_onboarded")
}

suspend fun setHasOnboarded(context: Context) {
    context.dataStore.edit { prefs ->
        prefs[PrefKey.HAS_ONBOARDED] = true
    }
}