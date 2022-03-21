package com.nagopy.android.debugassistant.domain.usecase.interactor

import com.nagopy.android.debugassistant.data.repository.UserPreferencesRepository
import com.nagopy.android.debugassistant.domain.ProxyInfo
import com.nagopy.android.debugassistant.domain.usecase.PutUserProxyInfoUseCase

class PutUserProxyInfoInteractor(
    private val userPreferencesRepository: UserPreferencesRepository
) : PutUserProxyInfoUseCase {

    override fun putUserProxyInfo(proxyInfo: ProxyInfo) {
        userPreferencesRepository.proxyHost = proxyInfo.host
        userPreferencesRepository.proxyPort = proxyInfo.port
    }
}
