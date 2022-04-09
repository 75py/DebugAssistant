package com.nagopy.android.debugassistant.domain.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.data.repository.GlobalSettingsRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GetAdbStatusInteractorTest {
    private lateinit var getAdbStatusInteractor: GetAdbStatusInteractor
    private lateinit var globalSettingsRepository: GlobalSettingsRepository

    @Before
    fun setUp() {
        globalSettingsRepository = mockk(relaxed = true)
        getAdbStatusInteractor =
            GetAdbStatusInteractor(globalSettingsRepository)
    }

    @Test
    fun isProxyEnabled() {
        every {
            globalSettingsRepository.getInt(
                any(),
                any()
            )
        } returns GlobalSettingsRepository.SETTING_OFF
        kotlin.test.assertFalse(getAdbStatusInteractor.isAdbEnabled())
        verify {
            globalSettingsRepository.getInt(
                Settings.Global.ADB_ENABLED,
                GlobalSettingsRepository.SETTING_OFF
            )
        }

        clearAllMocks()

        every {
            globalSettingsRepository.getInt(
                any(),
                any()
            )
        } returns GlobalSettingsRepository.SETTING_ON
        kotlin.test.assertTrue(getAdbStatusInteractor.isAdbEnabled())
        verify {
            globalSettingsRepository.getInt(
                Settings.Global.ADB_ENABLED,
                GlobalSettingsRepository.SETTING_OFF
            )
        }
    }
}
