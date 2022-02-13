package com.nagopy.android.debugassistant.usecase

interface GetPermissionStatusUseCase {

    fun isPermissionGranted(permission: String): Boolean
}
