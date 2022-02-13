package com.nagopy.android.debugassistant.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.repository.UserPreferencesRepository
import com.nagopy.android.debugassistant.usecase.DisableProxyUseCase

class DisableProxyInteractor(
    private val globalSettingsRepository: GlobalSettingsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : DisableProxyUseCase {

    override fun disableProxy(): Boolean {
        val ret = globalSettingsRepository.putString(
            Settings.Global.HTTP_PROXY,
            GlobalSettingsRepository.DISABLE_PROXY_VALUE
        )
        if (ret) {
            userPreferencesRepository.proxyHost = ""
            userPreferencesRepository.proxyPort = ""
        }
        return ret
    }
}
