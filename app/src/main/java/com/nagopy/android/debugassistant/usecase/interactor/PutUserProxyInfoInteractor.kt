package com.nagopy.android.debugassistant.usecase.interactor

import com.nagopy.android.debugassistant.domain.ProxyInfo
import com.nagopy.android.debugassistant.repository.UserPreferencesRepository
import com.nagopy.android.debugassistant.usecase.GetUserProxyInfoUseCase
import com.nagopy.android.debugassistant.usecase.PutUserProxyInfoUseCase

class PutUserProxyInfoInteractor(
    private val userPreferencesRepository: UserPreferencesRepository
) : PutUserProxyInfoUseCase {

    override fun putUserProxyInfo(proxyInfo: ProxyInfo) {
        userPreferencesRepository.proxyHost = proxyInfo.host
        userPreferencesRepository.proxyPort = proxyInfo.port
    }
}
