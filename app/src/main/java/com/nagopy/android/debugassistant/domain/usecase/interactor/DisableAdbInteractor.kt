package com.nagopy.android.debugassistant.domain.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.data.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.domain.usecase.DisableAdbUseCase

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
