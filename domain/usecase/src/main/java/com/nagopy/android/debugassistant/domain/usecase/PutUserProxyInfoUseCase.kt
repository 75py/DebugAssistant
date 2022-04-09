package com.nagopy.android.debugassistant.domain.usecase

import com.nagopy.android.debugassistant.domain.model.ProxyInfo

interface PutUserProxyInfoUseCase {
    fun putUserProxyInfo(proxyInfo: ProxyInfo)
}
