package com.nagopy.android.debugassistant.domain.usecase.interactor

import com.nagopy.android.debugassistant.data.repository.UserPreferencesRepository
import com.nagopy.android.debugassistant.domain.ProxyInfo
import com.nagopy.android.debugassistant.domain.usecase.GetUserProxyInfoUseCase

class GetUserProxyInfoInteractor(
    private val userPreferencesRepository: UserPreferencesRepository
) : GetUserProxyInfoUseCase {

    override fun getUserProxyInfo(): ProxyInfo {
        return ProxyInfo(userPreferencesRepository.proxyHost, userPreferencesRepository.proxyPort)
    }
}
