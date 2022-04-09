package com.nagopy.android.debugassistant.domain.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.data.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.domain.usecase.DisableProxyUseCase

class DisableProxyInteractor(
    private val globalSettingsRepository: GlobalSettingsRepository
) : DisableProxyUseCase {

    override fun disableProxy(): Boolean {
        return globalSettingsRepository.putString(
            Settings.Global.HTTP_PROXY,
            GlobalSettingsRepository.DISABLE_PROXY_VALUE
        )
    }
}
