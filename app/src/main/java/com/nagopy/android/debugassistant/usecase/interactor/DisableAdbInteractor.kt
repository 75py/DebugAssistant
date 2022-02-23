package com.nagopy.android.debugassistant.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.usecase.DisableAdbUseCase

class DisableAdbInteractor(
    private val globalSettingsRepository: GlobalSettingsRepository
) : DisableAdbUseCase {

    override fun disableAdb(): Boolean {
        return globalSettingsRepository.putInt(
            Settings.Global.ADB_ENABLED,
            GlobalSettingsRepository.SETTING_OFF
        )
    }
}
