package com.nagopy.android.debugassistant.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.usecase.GetProxyStatusUseCase

class GetProxyStatusInteractor(
    private val globalSettingsRepository: GlobalSettingsRepository,
) : GetProxyStatusUseCase {

    override fun isProxyEnabled(): Boolean {
        val currentValue = globalSettingsRepository.getString(Settings.Global.HTTP_PROXY)
        return !currentValue.isNullOrEmpty() && currentValue != GlobalSettingsRepository.DISABLE_PROXY_VALUE
    }

}
