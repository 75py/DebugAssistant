package com.nagopy.android.debugassistant.data.repository

interface GlobalSettingsRepository {

    fun putString(name: String, value: String?): Boolean

    fun getString(name: String): String?

    fun putInt(name: String, value: Int): Boolean

    fun getInt(name: String, def: Int): Int

    companion object {
        const val DISABLE_PROXY_VALUE = ":0"
        const val SETTING_ON = 1
        const val SETTING_OFF = 0
    }
}
