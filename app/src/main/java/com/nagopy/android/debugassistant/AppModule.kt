package com.nagopy.android.debugassistant

import android.content.ContentResolver
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ContentResolver> { androidContext().contentResolver }

    viewModel { MainViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}
