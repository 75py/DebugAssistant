package com.nagopy.android.debugassistant.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.repository.UserPreferencesRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DisableProxyInteractorTest {

    private lateinit var disableProxyInteractor: DisableProxyInteractor
    private lateinit var globalSettingsRepository: GlobalSettingsRepository
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    @Before
    fun setUp() {
        globalSettingsRepository = mockk(relaxed = true)
        userPreferencesRepository = mockk(relaxed = true)
        disableProxyInteractor =
            DisableProxyInteractor(globalSettingsRepository, userPreferencesRepository)
    }

    @Test
    fun disableProxy_putSuccess() {
        every { globalSettingsRepository.putString(any(), any()) } returns true
        val ret = disableProxyInteractor.disableProxy()
        assertTrue(ret)
        verifyAll {
            globalSettingsRepository.putString(
                Settings.Global.HTTP_PROXY,
                GlobalSettingsRepository.DISABLE_PROXY_VALUE
            )
            userPreferencesRepository.proxyHost = ""
            userPreferencesRepository.proxyPort = ""
        }
    }

    @Test
    fun disableProxy_putError() {
        every { globalSettingsRepository.putString(any(), any()) } returns false
        val ret2 = disableProxyInteractor.disableProxy()
        assertFalse(ret2)
        verify {
            globalSettingsRepository.putString(
                Settings.Global.HTTP_PROXY,
                GlobalSettingsRepository.DISABLE_PROXY_VALUE
            )
        }
        verify(inverse = true) {
            userPreferencesRepository.proxyHost = ""
            userPreferencesRepository.proxyPort = ""
        }
    }
}