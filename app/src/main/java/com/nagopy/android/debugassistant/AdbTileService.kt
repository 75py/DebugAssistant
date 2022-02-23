package com.nagopy.android.debugassistant

import android.Manifest
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.nagopy.android.debugassistant.usecase.DisableAdbUseCase
import com.nagopy.android.debugassistant.usecase.EnableAdbUseCase
import com.nagopy.android.debugassistant.usecase.GetAdbStatusUseCase
import com.nagopy.android.debugassistant.usecase.GetPermissionStatusUseCase
import org.koin.android.ext.android.inject

class AdbTileService : TileService() {

    private val getPermissionStatusUseCase: GetPermissionStatusUseCase by inject()
    private val getAdbStatusUseCase: GetAdbStatusUseCase by inject()
    private val enableAdbUseCase: EnableAdbUseCase by inject()
    private val disableAdbUseCase: DisableAdbUseCase by inject()

    private fun refresh() {
        qsTile.state =
            if (getPermissionStatusUseCase.isPermissionGranted(Manifest.permission.WRITE_SECURE_SETTINGS)) {
                if (getAdbStatusUseCase.isAdbEnabled()) {
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
            Settings.Global.getUriFor(Settings.Global.ADB_ENABLED),
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
                disableAdbUseCase.disableAdb()
            }
            Tile.STATE_INACTIVE -> {
                enableAdbUseCase.enableAdb()
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
