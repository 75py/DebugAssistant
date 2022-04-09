package com.nagopy.android.debugassistant.domain.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.data.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.domain.usecase.EnableAdbUseCase

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
