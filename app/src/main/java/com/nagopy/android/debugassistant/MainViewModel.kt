package com.nagopy.android.debugassistant

import android.Manifest
import android.app.Activity
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nagopy.android.debugassistant.repository.UserPreferencesRepository
import com.nagopy.android.debugassistant.usecase.DisableAdbWifiUseCase
import com.nagopy.android.debugassistant.usecase.DisableProxyUseCase
import com.nagopy.android.debugassistant.usecase.EnableAdbWifiUseCase
import com.nagopy.android.debugassistant.usecase.EnableProxyUseCase
import com.nagopy.android.debugassistant.usecase.GetAdbWifiStatusUseCase
import com.nagopy.android.debugassistant.usecase.GetPermissionStatusUseCase
import com.nagopy.android.debugassistant.usecase.GetProxyStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(
    private val getProxyStatusUseCase: GetProxyStatusUseCase,
    private val enableProxyUseCase: EnableProxyUseCase,
    private val disableProxyUseCase: DisableProxyUseCase,
    private val getAdbWifiStatusUseCase: GetAdbWifiStatusUseCase,
    private val enableAdbWifiUseCase: EnableAdbWifiUseCase,
    private val disableAdbWifiUseCase: DisableAdbWifiUseCase,
    private val getPermissionStatusUseCase: GetPermissionStatusUseCase,
    private val userPreferencesRepository: UserPreferencesRepository, // TODO どうするか考える
) : ViewModel() {

    val proxyHostFlow = MutableStateFlow(userPreferencesRepository.proxyHost)
    val proxyPortFlow = MutableStateFlow(userPreferencesRepository.proxyPort)

    init {
        viewModelScope.launch {
            proxyHostFlow.collect {
                userPreferencesRepository.proxyHost = it
            }
        }
        viewModelScope.launch {
            proxyPortFlow.collect {
                userPreferencesRepository.proxyPort = it
            }
        }
    }

    val isPermissionGranted = MutableStateFlow(false)
    val isProxyEnabled = MutableStateFlow(false)
    val isAdbWifiEnabled = MutableStateFlow(false)

    fun updateStatus() {
        updatePermissionStatus()
        updateProxyStatus()
        updateAdbWifiStatus()
    }

    private fun updateProxyStatus() {
        isProxyEnabled.value = getProxyStatusUseCase.isProxyEnabled()
    }

    private fun updateAdbWifiStatus() {
        isAdbWifiEnabled.value = getAdbWifiStatusUseCase.isAdbWifiEnabled()
    }

    private fun updatePermissionStatus() {
        isPermissionGranted.value =
            getPermissionStatusUseCase.isPermissionGranted(Manifest.permission.WRITE_SECURE_SETTINGS)
    }

    fun onAdbCommandClicked(activity: Activity) {
        activity.startActivity(
            ShareCompat.IntentBuilder(activity)
                .setText("adb shell pm grant ${BuildConfig.APPLICATION_ID} android.permission.WRITE_SECURE_SETTINGS")
                .setType("text/plain")
                .intent
        )
    }

    fun onProxySwitchClicked(checked: Boolean) {
        if (checked) {
            enableProxyUseCase.enableProxy(proxyHostFlow.value, proxyPortFlow.value)
        } else {
            disableProxyUseCase.disableProxy()
        }

        updateProxyStatus()
    }

    fun onAdbWifiSwitchClicked(checked: Boolean) {
        if (checked) {
            enableAdbWifiUseCase.enableAdbWifi()
        } else {
            disableAdbWifiUseCase.disableAdbWifi()
        }

        updateAdbWifiStatus()
    }
}
