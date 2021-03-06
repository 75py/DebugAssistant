package com.nagopy.android.debugassistant.domain.model

data class ProxyInfo(
    val host: String,
    val port: String
) {
    fun isAvailable(): Boolean {
        return host.isNotEmpty() && port.isNotEmpty()
    }
}
