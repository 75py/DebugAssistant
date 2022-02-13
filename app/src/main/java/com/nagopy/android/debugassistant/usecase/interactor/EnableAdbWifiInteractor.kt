package com.nagopy.android.debugassistant.usecase.interactor

import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.usecase.EnableAdbWifiUseCase

class EnableAdbWifiInteractor(
    private val globalSettingsRepository: GlobalSettingsRepository,
) : EnableAdbWifiUseCase {

    override fun enableAdbWifi(): Boolean {
        return globalSettingsRepository.putInt(
            GlobalSettingsRepository.ADB_WIFI_ENABLED,
            GlobalSettingsRepository.SETTING_ON
        )
    }
}
