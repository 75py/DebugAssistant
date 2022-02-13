package com.nagopy.android.debugassistant.usecase.interactor

import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class DisableAdbWifiInteractorTest {

    private lateinit var disableAdbWifiInteractor: DisableAdbWifiInteractor
    private lateinit var globalSettingsRepository: GlobalSettingsRepository

    @Before
    fun setUp() {
        globalSettingsRepository = mockk(relaxed = true)
        disableAdbWifiInteractor = DisableAdbWifiInteractor(globalSettingsRepository)
    }

    @Test
    fun disableAdbWifi_putSuccess() {
        every { globalSettingsRepository.putInt(any(), any()) } returns true
        val ret = disableAdbWifiInteractor.disableAdbWifi()
        kotlin.test.assertTrue(ret)
        verify {
            globalSettingsRepository.putInt(
                GlobalSettingsRepository.ADB_WIFI_ENABLED,
                GlobalSettingsRepository.SETTING_OFF
            )
        }
    }

    @Test
    fun disableAdbWifi_putError() {
        every { globalSettingsRepository.putInt(any(), any()) } returns false
        val ret = disableAdbWifiInteractor.disableAdbWifi()
        kotlin.test.assertFalse(ret)
        verify {
            globalSettingsRepository.putInt(
                GlobalSettingsRepository.ADB_WIFI_ENABLED,
                GlobalSettingsRepository.SETTING_OFF
            )
        }
    }
}