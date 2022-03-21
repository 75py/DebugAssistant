package com.nagopy.android.debugassistant.domain.usecase

interface EnableProxyUseCase {
    fun enableProxy(host: String, port: String): Boolean
}
