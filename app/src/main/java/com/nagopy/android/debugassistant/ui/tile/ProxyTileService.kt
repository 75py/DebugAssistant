package com.nagopy.android.debugassistant.ui.tile

import android.Manifest
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.nagopy.android.debugassistant.domain.usecase.DisableProxyUseCase
import com.nagopy.android.debugassistant.domain.usecase.EnableProxyUseCase
import com.nagopy.android.debugassistant.domain.usecase.GetPermissionStatusUseCase
import com.nagopy.android.debugassistant.domain.usecase.GetProxyStatusUseCase
import com.nagopy.android.debugassistant.domain.usecase.GetUserProxyInfoUseCase
import org.koin.android.ext.android.inject

class ProxyTileService : TileService() {

    private val getPermissionStatusUseCase: GetPermissionStatusUseCase by inject()
    private val getProxyStatusUseCase: GetProxyStatusUseCase by inject()
    private val enableProUseCase: EnableProxyUseCase by inject()
    private val disableProxyUseCase: DisableProxyUseCase by inject()
    private val getUserProxyInfoUseCase: GetUserProxyInfoUseCase by inject()

    private fun refresh() {
        qsTile.state =
            if (getPermissionStatusUseCase.isPermissionGranted(Manifest.permission.WRITE_SECURE_SETTINGS) &&
                getUserProxyInfoUseCase.getUserProxyInfo().isAvailable()
            ) {
                if (getProxyStatusUseCase.isProxyEnabled()) {
                    Tile.STATE_ACTIVE
                } else {
                    Tile.STATE_INACTIVE
                }
            } else {
                Tile.STATE_UNAVAILABLE
            }
        qsTile.updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()
        refresh()
        contentResolver.registerContentObserver(
            Settings.Global.getUriFor(Settings.Global.HTTP_PROXY),
            false,
            settingsObserver
        )
    }

    override fun onStopListening() {
        super.onStopListening()
        contentResolver.unregisterContentObserver(settingsObserver)
    }

    override fun onClick() {
        super.onClick()
        when (qsTile.state) {
            Tile.STATE_ACTIVE -> {
                disableProxyUseCase.disableProxy()
            }
            Tile.STATE_INACTIVE -> {
                val proxyInfo = getUserProxyInfoUseCase.getUserProxyInfo()
                enableProUseCase.enableProxy(
                    proxyInfo.host,
                    proxyInfo.port
                )
            }
        }
        refresh()
    }

    private val handler: Handler = Handler(Looper.getMainLooper())
    private val settingsObserver: ContentObserver = object : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            refresh()
        }
    }
}
