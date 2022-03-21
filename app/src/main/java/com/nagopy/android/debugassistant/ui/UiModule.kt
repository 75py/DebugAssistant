package com.nagopy.android.debugassistant.ui

import com.nagopy.android.debugassistant.ui.main.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { MainViewModel(androidApplication(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}
