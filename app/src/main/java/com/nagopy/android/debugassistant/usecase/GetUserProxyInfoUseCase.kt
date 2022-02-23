package com.nagopy.android.debugassistant.usecase

import com.nagopy.android.debugassistant.domain.ProxyInfo

interface GetUserProxyInfoUseCase {
    fun getUserProxyInfo(): ProxyInfo
}
