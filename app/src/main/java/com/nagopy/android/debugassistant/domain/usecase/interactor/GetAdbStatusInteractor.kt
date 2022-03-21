package com.nagopy.android.debugassistant.domain.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.data.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.domain.usecase.GetAdbStatusUseCase

class GetAdbStatusInteractor(
    private val globalSettingsRepository: GlobalSettingsRepository,
) : GetAdbStatusUseCase {

    override fun isAdbEnabled(): Boolean {
        return globalSettingsRepository.getInt(
            Settings.Global.ADB_ENABLED,
            GlobalSettingsRepository.SETTING_OFF
        ) != GlobalSettingsRepository.SETTING_OFF
    }
}
