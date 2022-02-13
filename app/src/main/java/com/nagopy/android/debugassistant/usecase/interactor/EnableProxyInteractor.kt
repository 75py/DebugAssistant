package com.nagopy.android.debugassistant.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.repository.UserPreferencesRepository
import com.nagopy.android.debugassistant.usecase.EnableProxyUseCase

class EnableProxyInteractor(
    private val globalSettingsRepository: GlobalSettingsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : EnableProxyUseCase {

    override fun enableProxy(host: String, port: String): Boolean {
        val ret = globalSettingsRepository.putString(Settings.Global.HTTP_PROXY, "$host:$port")
        if (ret) {
            userPreferencesRepository.proxyHost = host
            userPreferencesRepository.proxyPort = port
        }
        return ret
    }
}
