package com.nagopy.android.debugassistant.ui.main

data class MainUiState(
    val isLoading: Boolean,
    val proxyHost: String = "",
    val proxyPort: String = "",
    val isPermissionGranted: Boolean = false,
    val isProxyEnabled: Boolean = false,
    val isAdbEnabled: Boolean = false,
)
