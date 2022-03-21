package com.nagopy.android.debugassistant.domain.usecase

import com.nagopy.android.debugassistant.domain.ProxyInfo

interface GetUserProxyInfoUseCase {
    fun getUserProxyInfo(): ProxyInfo
}
