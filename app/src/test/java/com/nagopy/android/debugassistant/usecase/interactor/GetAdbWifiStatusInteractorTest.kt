package com.nagopy.android.debugassistant.usecase.interactor

import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GetAdbWifiStatusInteractorTest {
    private lateinit var getAdbWifiStatusInteractor: GetAdbWifiStatusInteractor
    private lateinit var globalSettingsRepository: GlobalSettingsRepository

    @Before
    fun setUp() {
        globalSettingsRepository = mockk(relaxed = true)
        getAdbWifiStatusInteractor =
            GetAdbWifiStatusInteractor(globalSettingsRepository)
    }

    @Test
    fun isProxyEnabled() {
        every { globalSettingsRepository.getInt(any(), any()) } returns GlobalSettingsRepository.SETTING_OFF
        kotlin.test.assertFalse(getAdbWifiStatusInteractor.isAdbWifiEnabled())
        verify {
            globalSettingsRepository.getInt(
                GlobalSettingsRepository.ADB_WIFI_ENABLED,
                GlobalSettingsRepository.SETTING_OFF
            )
        }

        clearAllMocks()

        every { globalSettingsRepository.getInt(any(), any()) } returns GlobalSettingsRepository.SETTING_ON
        kotlin.test.assertTrue(getAdbWifiStatusInteractor.isAdbWifiEnabled())
        verify {
            globalSettingsRepository.getInt(
                GlobalSettingsRepository.ADB_WIFI_ENABLED,
                GlobalSettingsRepository.SETTING_OFF
            )
        }
    }
}