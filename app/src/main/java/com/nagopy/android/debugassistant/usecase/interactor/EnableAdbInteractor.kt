package com.nagopy.android.debugassistant.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.usecase.EnableAdbUseCase

class EnableAdbInteractor(
    private val globalSettingsRepository: GlobalSettingsRepository,
) : EnableAdbUseCase {

    override fun enableAdb(): Boolean {
        return globalSettingsRepository.putInt(
            Settings.Global.ADB_ENABLED,
            GlobalSettingsRepository.SETTING_ON
        )
    }
}
