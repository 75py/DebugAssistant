package com.nagopy.android.debugassistant.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DisableProxyInteractorTest {

    private lateinit var disableProxyInteractor: DisableProxyInteractor
    private lateinit var globalSettingsRepository: GlobalSettingsRepository

    @Before
    fun setUp() {
        globalSettingsRepository = mockk(relaxed = true)
        disableProxyInteractor =
            DisableProxyInteractor(globalSettingsRepository)
    }

    @Test
    fun disableProxy_putSuccess() {
        every { globalSettingsRepository.putString(any(), any()) } returns true
        val ret = disableProxyInteractor.disableProxy()
        assertTrue(ret)
        verify {
            globalSettingsRepository.putString(
                Settings.Global.HTTP_PROXY,
                GlobalSettingsRepository.DISABLE_PROXY_VALUE
            )
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
    }
}
