package com.nagopy.android.debugassistant.data

import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.repository.UserPreferencesRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {
    single<GlobalSettingsRepository> { GlobalSettingRepositoryImpl(get()) }

    single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(androidApplication().applicationContext) }
}
