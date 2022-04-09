package com.nagopy.android.debugassistant.data.repository

import com.nagopy.android.debugassistant.data.repository.impl.GlobalSettingRepositoryImpl
import com.nagopy.android.debugassistant.data.repository.impl.UserPreferencesRepositoryImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repositoryModule = module {
    single<GlobalSettingsRepository> { GlobalSettingRepositoryImpl(get()) }

    single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(androidApplication().applicationContext) }
}
