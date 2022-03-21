package com.nagopy.android.debugassistant

import android.app.Application
import com.nagopy.android.debugassistant.data.dataModule
import com.nagopy.android.debugassistant.domain.domainModule
import com.nagopy.android.debugassistant.ui.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DebugAssistantApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // androidLogger()
            androidContext(this@DebugAssistantApplication)
            modules(appModule, uiModule, domainModule, dataModule)
        }
    }
}
