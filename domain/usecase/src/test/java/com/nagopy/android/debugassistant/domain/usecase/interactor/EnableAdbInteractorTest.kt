package com.nagopy.android.debugassistant.domain.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.data.repository.GlobalSettingsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class EnableAdbInteractorTest {

    private lateinit var enableAdbInteractor: EnableAdbInteractor
    private lateinit var globalSettingsRepository: GlobalSettingsRepository

    @Before
    fun setUp() {
        globalSettingsRepository = mockk(relaxed = true)
        enableAdbInteractor = EnableAdbInteractor(globalSettingsRepository)
    }

    @Test
    fun enableAdb_putSuccess() {
        every { globalSettingsRepository.putInt(any(), any()) } returns true
        val ret = enableAdbInteractor.enableAdb()
        kotlin.test.assertTrue(ret)
        verify {
            globalSettingsRepository.putInt(
                Settings.Global.ADB_ENABLED,
                GlobalSettingsRepository.SETTING_ON
            )
        }
    }

    @Test
    fun enableAdb_putError() {
        every { globalSettingsRepository.putInt(any(), any()) } returns false
        val ret = enableAdbInteractor.enableAdb()
        kotlin.test.assertFalse(ret)
        verify {
            globalSettingsRepository.putInt(
                Settings.Global.ADB_ENABLED,
                GlobalSettingsRepository.SETTING_ON
            )
        }
    }
}
