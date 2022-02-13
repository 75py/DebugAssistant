package com.nagopy.android.debugassistant.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.nagopy.android.debugassistant.repository.UserPreferencesRepository


class UserPreferencesRepositoryImpl(context: Context) : UserPreferencesRepository {
    private val sharedPreferences: SharedPreferences

    init {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        sharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )


    }

    override var proxyHost: String
        get() = sharedPreferences.getString("proxyHost", "") ?: ""
        set(value) {
            sharedPreferences.edit().putString("proxyHost", value).apply()
        }
    override var proxyPort: String
        get() = sharedPreferences.getString("proxyPort", "") ?: ""
        set(value) {
            sharedPreferences.edit().putString("proxyPort", value).apply()
        }

}