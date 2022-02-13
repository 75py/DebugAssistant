package com.nagopy.android.debugassistant.usecase.interactor

import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.usecase.GetAdbWifiStatusUseCase

class GetAdbWifiStatusInteractor(
    private val globalSettingsRepository: GlobalSettingsRepository,
) : GetAdbWifiStatusUseCase {

    override fun isAdbWifiEnabled(): Boolean {
        return globalSettingsRepository.getInt(
            GlobalSettingsRepository.ADB_WIFI_ENABLED,
            GlobalSettingsRepository.SETTING_OFF
        ) != GlobalSettingsRepository.SETTING_OFF
    }
}
