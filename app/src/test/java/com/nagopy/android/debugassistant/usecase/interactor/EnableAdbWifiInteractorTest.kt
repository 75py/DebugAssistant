package com.nagopy.android.debugassistant.usecase.interactor

import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class EnableAdbWifiInteractorTest {

    private lateinit var enableAdbWifiInteractor: EnableAdbWifiInteractor
    private lateinit var globalSettingsRepository: GlobalSettingsRepository

    @Before
    fun setUp() {
        globalSettingsRepository = mockk(relaxed = true)
        enableAdbWifiInteractor = EnableAdbWifiInteractor(globalSettingsRepository)
    }

    @Test
    fun enableAdbWifi_putSuccess() {
        every { globalSettingsRepository.putInt(any(), any()) } returns true
        val ret = enableAdbWifiInteractor.enableAdbWifi()
        kotlin.test.assertTrue(ret)
        verify {
            globalSettingsRepository.putInt(
                GlobalSettingsRepository.ADB_WIFI_ENABLED,
                GlobalSettingsRepository.SETTING_ON
            )
        }
    }

    @Test
    fun enableAdbWifi_putError() {
        every { globalSettingsRepository.putInt(any(), any()) } returns false
        val ret = enableAdbWifiInteractor.enableAdbWifi()
        kotlin.test.assertFalse(ret)
        verify {
            globalSettingsRepository.putInt(
                GlobalSettingsRepository.ADB_WIFI_ENABLED,
                GlobalSettingsRepository.SETTING_ON
            )
        }
    }
}
