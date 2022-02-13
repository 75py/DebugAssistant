package com.nagopy.android.debugassistant.usecase

import com.nagopy.android.debugassistant.usecase.interactor.DisableAdbWifiInteractor
import com.nagopy.android.debugassistant.usecase.interactor.DisableProxyInteractor
import com.nagopy.android.debugassistant.usecase.interactor.EnableAdbWifiInteractor
import com.nagopy.android.debugassistant.usecase.interactor.EnableProxyInteractor
import com.nagopy.android.debugassistant.usecase.interactor.GetAdbWifiStatusInteractor
import com.nagopy.android.debugassistant.usecase.interactor.GetPermissionStatusInteractor
import com.nagopy.android.debugassistant.usecase.interactor.GetProxyStatusInteractor
import org.koin.dsl.module

val useCaseModule = module {

    single<GetProxyStatusUseCase> { GetProxyStatusInteractor(get()) }

    single<EnableProxyUseCase> { EnableProxyInteractor(get(), get()) }

    single<DisableProxyUseCase> { DisableProxyInteractor(get(), get()) }

    single<GetAdbWifiStatusUseCase> { GetAdbWifiStatusInteractor(get()) }

    single<EnableAdbWifiUseCase> { EnableAdbWifiInteractor(get()) }

    single<DisableAdbWifiUseCase> { DisableAdbWifiInteractor(get()) }

    single<GetPermissionStatusUseCase> { GetPermissionStatusInteractor(get()) }
}
