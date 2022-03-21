package com.nagopy.android.debugassistant.usecase

import com.nagopy.android.debugassistant.domain.ProxyInfo

interface PutUserProxyInfoUseCase {
    fun putUserProxyInfo(proxyInfo: ProxyInfo)
}
