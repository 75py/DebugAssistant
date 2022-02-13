package com.nagopy.android.debugassistant.usecase.interactor

import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.usecase.DisableAdbWifiUseCase

class DisableAdbWifiInteractor(
    private val globalSettingsRepository: GlobalSettingsRepository
) : DisableAdbWifiUseCase {

    override fun disableAdbWifi(): Boolean {
        return globalSettingsRepository.putInt(
            GlobalSettingsRepository.ADB_WIFI_ENABLED,
            GlobalSettingsRepository.SETTING_OFF
        )
    }
}
