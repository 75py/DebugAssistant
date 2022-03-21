package com.nagopy.android.debugassistant.domain.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.data.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.domain.usecase.EnableProxyUseCase

class EnableProxyInteractor(
    private val globalSettingsRepository: GlobalSettingsRepository
) : EnableProxyUseCase {

    override fun enableProxy(host: String, port: String): Boolean {
        return globalSettingsRepository.putString(Settings.Global.HTTP_PROXY, "$host:$port")
    }
}
