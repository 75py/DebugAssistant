package com.nagopy.android.debugassistant.usecase

interface EnableProxyUseCase {
    fun enableProxy(host: String, port: String): Boolean
}
