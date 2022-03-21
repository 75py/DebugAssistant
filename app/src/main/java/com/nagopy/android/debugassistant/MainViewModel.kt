package com.nagopy.android.debugassistant

import android.Manifest
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.nagopy.android.debugassistant.domain.ProxyInfo
import com.nagopy.android.debugassistant.usecase.DisableAdbUseCase
import com.nagopy.android.debugassistant.usecase.DisableProxyUseCase
import com.nagopy.android.debugassistant.usecase.EnableAdbUseCase
import com.nagopy.android.debugassistant.usecase.EnableProxyUseCase
import com.nagopy.android.debugassistant.usecase.GetAdbStatusUseCase
import com.nagopy.android.debugassistant.usecase.GetPermissionStatusUseCase
import com.nagopy.android.debugassistant.usecase.GetProxyStatusUseCase
import com.nagopy.android.debugassistant.usecase.GetUserProxyInfoUseCase
import com.nagopy.android.debugassistant.usecase.PutUserProxyInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MainViewModel(
    application: Application,
    private val getProxyStatusUseCase: GetProxyStatusUseCase,
    private val enableProxyUseCase: EnableProxyUseCase,
    private val disableProxyUseCase: DisableProxyUseCase,
    private val getAdbStatusUseCase: GetAdbStatusUseCase,
    private val enableAdbUseCase: EnableAdbUseCase,
    private val disableAdbUseCase: DisableAdbUseCase,
    private val getPermissionStatusUseCase: GetPermissionStatusUseCase,
    getUserProxyInfoUseCase: GetUserProxyInfoUseCase,
    private val putUserProxyInfoUseCase: PutUserProxyInfoUseCase,
) : AndroidViewModel(application) {

    private val _viewModelState = MutableStateFlow(MainViewModelState(isLoading = true))
    val viewModelState = _viewModelState.stateIn(viewModelScope, SharingStarted.Eagerly, _viewModelState.value)

    init {
        val proxyInfo = getUserProxyInfoUseCase.getUserProxyInfo()
        val isPermissionGranted = getPermissionStatusUseCase.isPermissionGranted(Manifest.permission.WRITE_SECURE_SETTINGS)
        val isProxyEnabled = getProxyStatusUseCase.isProxyEnabled()
        val isAdbEnabled = getAdbStatusUseCase.isAdbEnabled()
        _viewModelState.update {
            it.copy(
                isLoading = false,
                proxyHost = proxyInfo.host,
                proxyPort = proxyInfo.port,
                isPermissionGranted = isPermissionGranted,
                isProxyEnabled = isProxyEnabled,
                isAdbEnabled = isAdbEnabled,
            )
        }
    }

    fun updateStatus() {
        updatePermissionStatus()
        updateProxyStatus()
        updateAdbStatus()
    }

    private fun updateProxyStatus() {
        _viewModelState.update {
            it.copy(
                isProxyEnabled = getProxyStatusUseCase.isProxyEnabled()
            )
        }
    }

    private fun updateAdbStatus() {
        _viewModelState.update {
            it.copy(
                isAdbEnabled = getAdbStatusUseCase.isAdbEnabled()
            )
        }
    }

    private fun updatePermissionStatus() {
        _viewModelState.update {
            it.copy(
                isPermissionGranted = getPermissionStatusUseCase.isPermissionGranted(Manifest.permission.WRITE_SECURE_SETTINGS)
            )
        }
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

    fun onProxyHostChanged(newValue: String) {
        putUserProxyInfoUseCase.putUserProxyInfo(ProxyInfo(newValue, _viewModelState.value.proxyPort))
        _viewModelState.update {
            it.copy(
                proxyHost = newValue
            )
        }
    }

    fun onProxyPortChanged(newValue: String) {
        putUserProxyInfoUseCase.putUserProxyInfo(ProxyInfo(_viewModelState.value.proxyHost, newValue))
        _viewModelState.update {
            it.copy(
                proxyPort = newValue
            )
        }
    }

    fun onProxySwitchClicked(checked: Boolean) {
        if (checked) {
            enableProxyUseCase.enableProxy(viewModelState.value.proxyHost, viewModelState.value.proxyPort)
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

    fun onHowToUseButtonClicked() {
        getApplication<Application>().let { application ->
            application.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/75py/DebugAssistant#install"))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }

    fun onLicensesButtonClicked() {
        getApplication<Application>().let { application ->
            application.startActivity(
                Intent(application, OssLicensesMenuActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }
}

data class MainViewModelState(
    val isLoading: Boolean,
    val proxyHost: String = "",
    val proxyPort: String = "",
    val isPermissionGranted: Boolean = false,
    val isProxyEnabled: Boolean = false,
    val isAdbEnabled: Boolean = false,
)
