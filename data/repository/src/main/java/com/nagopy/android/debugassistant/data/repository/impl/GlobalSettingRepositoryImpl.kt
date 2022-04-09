package com.nagopy.android.debugassistant.data.repository.impl

import android.content.ContentResolver
import android.provider.Settings
import com.nagopy.android.debugassistant.data.repository.GlobalSettingsRepository

internal class GlobalSettingRepositoryImpl(private val resolver: ContentResolver) :
    GlobalSettingsRepository {

    override fun putString(name: String, value: String?): Boolean {
        return Settings.Global.putString(resolver, name, value)
    }

    override fun getString(name: String): String? {
        return Settings.Global.getString(resolver, name)
    }

    override fun putInt(name: String, value: Int): Boolean {
        return Settings.Global.putInt(resolver, name, value)
    }

    override fun getInt(name: String, def: Int): Int {
        return Settings.Global.getInt(resolver, name, def)
    }
}
