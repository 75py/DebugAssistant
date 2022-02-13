package com.nagopy.android.debugassistant.usecase.interactor

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.nagopy.android.debugassistant.usecase.GetPermissionStatusUseCase

class GetPermissionStatusInteractor(
    private val context: Context
) : GetPermissionStatusUseCase {
    override fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}
