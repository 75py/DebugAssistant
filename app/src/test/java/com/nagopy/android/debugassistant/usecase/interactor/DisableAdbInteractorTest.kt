package com.nagopy.android.debugassistant.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class DisableAdbInteractorTest {

    private lateinit var disableAdbInteractor: DisableAdbInteractor
    private lateinit var globalSettingsRepository: GlobalSettingsRepository

    @Before
    fun setUp() {
        globalSettingsRepository = mockk(relaxed = true)
        disableAdbInteractor = DisableAdbInteractor(globalSettingsRepository)
    }

    @Test
    fun disableAdb_putSuccess() {
        every { globalSettingsRepository.putInt(any(), any()) } returns true
        val ret = disableAdbInteractor.disableAdb()
        kotlin.test.assertTrue(ret)
        verify {
            globalSettingsRepository.putInt(
                Settings.Global.ADB_ENABLED,
                GlobalSettingsRepository.SETTING_OFF
            )
        }
    }

    @Test
    fun disableAdb_putError() {
        every { globalSettingsRepository.putInt(any(), any()) } returns false
        val ret = disableAdbInteractor.disableAdb()
        kotlin.test.assertFalse(ret)
        verify {
            globalSettingsRepository.putInt(
                Settings.Global.ADB_ENABLED,
                GlobalSettingsRepository.SETTING_OFF
            )
        }
    }
}
