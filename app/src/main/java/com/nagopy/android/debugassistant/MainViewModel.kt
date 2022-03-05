package com.nagopy.android.debugassistant

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.core.app.ShareCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.nagopy.android.debugassistant.repository.UserPreferencesRepository
import com.nagopy.android.debugassistant.usecase.DisableAdbUseCase
import com.nagopy.android.debugassistant.usecase.DisableProxyUseCase
import com.nagopy.android.debugassistant.usecase.EnableAdbUseCase
import com.nagopy.android.debugassistant.usecase.EnableProxyUseCase
import com.nagopy.android.debugassistant.usecase.GetAdbStatusUseCase
import com.nagopy.android.debugassistant.usecase.GetPermissionStatusUseCase
import com.nagopy.android.debugassistant.usecase.GetProxyStatusUseCase
import com.nagopy.android.debugassistant.usecase.GetUserProxyInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val getProxyStatusUseCase: GetProxyStatusUseCase,
    private val enableProxyUseCase: EnableProxyUseCase,
    private val disableProxyUseCase: DisableProxyUseCase,
    private val getAdbStatusUseCase: GetAdbStatusUseCase,
    private val enableAdbUseCase: EnableAdbUseCase,
    private val disableAdbUseCase: DisableAdbUseCase,
    private val getPermissionStatusUseCase: GetPermissionStatusUseCase,
    private val getUserProxyInfoUseCase: GetUserProxyInfoUseCase,
    private val userPreferencesRepository: UserPreferencesRepository, // TODO どうするか考える
) : AndroidViewModel(application) {

    val proxyHostFlow: MutableStateFlow<String>
    val proxyPortFlow: MutableStateFlow<String>

    init {
        val proxyInfo = getUserProxyInfoUseCase.getUserProxyInfo()
        proxyHostFlow = MutableStateFlow(proxyInfo.host)
        proxyPortFlow = MutableStateFlow(proxyInfo.port)

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
    val isAdbEnabled = MutableStateFlow(false)

    fun updateStatus() {
        updatePermissionStatus()
        updateProxyStatus()
        updateAdbStatus()
    }

    private fun updateProxyStatus() {
        isProxyEnabled.value = getProxyStatusUseCase.isProxyEnabled()
    }

    private fun updateAdbStatus() {
        isAdbEnabled.value = getAdbStatusUseCase.isAdbEnabled()
    }

    private fun updatePermissionStatus() {
        isPermissionGranted.value =
            getPermissionStatusUseCase.isPermissionGranted(Manifest.permission.WRITE_SECURE_SETTINGS)
    }

    fun onAdbCommandClicked() {
        getApplication<Application>().let { application ->
            application.startActivity(
                ShareCompat.IntentBuilder(application)
                    .setText("adb shell pm grant ${BuildConfig.APPLICATION_ID} android.permission.WRITE_SECURE_SETTINGS")
                    .setType("text/plain")
                    .intent
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }

    fun onProxySwitchClicked(checked: Boolean) {
        if (checked) {
            enableProxyUseCase.enableProxy(proxyHostFlow.value, proxyPortFlow.value)
        } else {
            disableProxyUseCase.disableProxy()
        }

        updateProxyStatus()
    }

    fun onAdbSwitchClicked(checked: Boolean) {
        if (checked) {
            enableAdbUseCase.enableAdb()
        } else {
            disableAdbUseCase.disableAdb()
        }

        updateAdbStatus()
    }

    fun onLicensesMenuClicked(activity: Activity) {
        activity.startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
    }
}
