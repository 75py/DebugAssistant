package com.nagopy.android.debugassistant.usecase

import com.nagopy.android.debugassistant.usecase.interactor.DisableAdbInteractor
import com.nagopy.android.debugassistant.usecase.interactor.DisableProxyInteractor
import com.nagopy.android.debugassistant.usecase.interactor.EnableAdbInteractor
import com.nagopy.android.debugassistant.usecase.interactor.EnableProxyInteractor
import com.nagopy.android.debugassistant.usecase.interactor.GetAdbStatusInteractor
import com.nagopy.android.debugassistant.usecase.interactor.GetPermissionStatusInteractor
import com.nagopy.android.debugassistant.usecase.interactor.GetProxyStatusInteractor
import com.nagopy.android.debugassistant.usecase.interactor.GetUserProxyInfoInteractor
import com.nagopy.android.debugassistant.usecase.interactor.PutUserProxyInfoInteractor
import org.koin.dsl.module

val useCaseModule = module {

    single<GetProxyStatusUseCase> { GetProxyStatusInteractor(get()) }

    single<EnableProxyUseCase> { EnableProxyInteractor(get()) }

    single<DisableProxyUseCase> { DisableProxyInteractor(get()) }

    single<GetAdbStatusUseCase> { GetAdbStatusInteractor(get()) }

    single<EnableAdbUseCase> { EnableAdbInteractor(get()) }

    single<DisableAdbUseCase> { DisableAdbInteractor(get()) }

    single<GetPermissionStatusUseCase> { GetPermissionStatusInteractor(get()) }

    single<GetUserProxyInfoUseCase> { GetUserProxyInfoInteractor(get()) }

    single<PutUserProxyInfoUseCase> { PutUserProxyInfoInteractor(get()) }
}
