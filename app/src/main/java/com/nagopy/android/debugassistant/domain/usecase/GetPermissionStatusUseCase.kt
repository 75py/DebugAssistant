package com.nagopy.android.debugassistant.domain.usecase

interface GetPermissionStatusUseCase {

    fun isPermissionGranted(permission: String): Boolean
}
