package com.nagopy.android.debugassistant.domain.usecase

import com.nagopy.android.debugassistant.domain.usecase.DisableAdbUseCase
import com.nagopy.android.debugassistant.domain.usecase.DisableProxyUseCase
import com.nagopy.android.debugassistant.domain.usecase.EnableAdbUseCase
import com.nagopy.android.debugassistant.domain.usecase.EnableProxyUseCase
import com.nagopy.android.debugassistant.domain.usecase.GetAdbStatusUseCase
import com.nagopy.android.debugassistant.domain.usecase.GetPermissionStatusUseCase
import com.nagopy.android.debugassistant.domain.usecase.GetProxyStatusUseCase
import com.nagopy.android.debugassistant.domain.usecase.GetUserProxyInfoUseCase
import com.nagopy.android.debugassistant.domain.usecase.PutUserProxyInfoUseCase
import com.nagopy.android.debugassistant.domain.usecase.interactor.DisableAdbInteractor
import com.nagopy.android.debugassistant.domain.usecase.interactor.DisableProxyInteractor
import com.nagopy.android.debugassistant.domain.usecase.interactor.EnableAdbInteractor
import com.nagopy.android.debugassistant.domain.usecase.interactor.EnableProxyInteractor
import com.nagopy.android.debugassistant.domain.usecase.interactor.GetAdbStatusInteractor
import com.nagopy.android.debugassistant.domain.usecase.interactor.GetPermissionStatusInteractor
import com.nagopy.android.debugassistant.domain.usecase.interactor.GetProxyStatusInteractor
import com.nagopy.android.debugassistant.domain.usecase.interactor.GetUserProxyInfoInteractor
import com.nagopy.android.debugassistant.domain.usecase.interactor.PutUserProxyInfoInteractor
import org.koin.dsl.module

val domainModule = module {

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
