package com.nagopy.android.debugassistant

import android.app.KeyguardManager
import android.content.ContentResolver
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<ContentResolver> { androidContext().contentResolver }

    single<KeyguardManager> { androidApplication().getSystemService(KeyguardManager::class.java) }
}
