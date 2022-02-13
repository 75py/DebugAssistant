package com.nagopy.android.debugassistant

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nagopy.android.debugassistant.data.GlobalSettingRepositoryImpl
import com.nagopy.android.debugassistant.data.UserPreferencesRepositoryImpl
import com.nagopy.android.debugassistant.usecase.DisableAdbWifiUseCase
import com.nagopy.android.debugassistant.usecase.DisableProxyUseCase
import com.nagopy.android.debugassistant.usecase.EnableAdbWifiUseCase
import com.nagopy.android.debugassistant.usecase.EnableProxyUseCase
import com.nagopy.android.debugassistant.usecase.GetAdbWifiStatusUseCase
import com.nagopy.android.debugassistant.usecase.GetProxyStatusUseCase
import com.nagopy.android.debugassistant.usecase.interactor.DisableAdbWifiInteractor
import com.nagopy.android.debugassistant.usecase.interactor.DisableProxyInteractor
import com.nagopy.android.debugassistant.usecase.interactor.EnableAdbWifiInteractor
import com.nagopy.android.debugassistant.usecase.interactor.EnableProxyInteractor
import com.nagopy.android.debugassistant.usecase.interactor.GetAdbWifiStatusInteractor
import com.nagopy.android.debugassistant.usecase.interactor.GetProxyStatusInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // TODO DI
    private val getProxyStatusUseCase: GetProxyStatusUseCase
    private val enableProxyUseCase: EnableProxyUseCase
    private val disableProxyUseCase: DisableProxyUseCase
    private val getAdbWifiStatusUseCase: GetAdbWifiStatusUseCase
    private val enableAdbWifiUseCase: EnableAdbWifiUseCase
    private val disableAdbWifiUseCase: DisableAdbWifiUseCase
    val proxyHostFlow: MutableStateFlow<String>
    val proxyPortFlow: MutableStateFlow<String>

    init {
        val globalSettingsRepository = GlobalSettingRepositoryImpl(application.contentResolver)
        val userPreferencesRepository =
            UserPreferencesRepositoryImpl(application.applicationContext)
        getProxyStatusUseCase = GetProxyStatusInteractor(globalSettingsRepository)
        enableProxyUseCase =
            EnableProxyInteractor(globalSettingsRepository, userPreferencesRepository)
        disableProxyUseCase =
            DisableProxyInteractor(globalSettingsRepository, userPreferencesRepository)
        getAdbWifiStatusUseCase = GetAdbWifiStatusInteractor(globalSettingsRepository)
        enableAdbWifiUseCase = EnableAdbWifiInteractor(globalSettingsRepository)
        disableAdbWifiUseCase = DisableAdbWifiInteractor(globalSettingsRepository)

        proxyHostFlow = MutableStateFlow(userPreferencesRepository.proxyHost)
        viewModelScope.launch {
            proxyHostFlow.collect {
                userPreferencesRepository.proxyHost = it
            }
        }
        proxyPortFlow = MutableStateFlow(userPreferencesRepository.proxyPort)
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
        isPermissionGranted.value = ContextCompat.checkSelfPermission(
            getApplication(),
            Manifest.permission.WRITE_SECURE_SETTINGS
        ) == PackageManager.PERMISSION_GRANTED
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
