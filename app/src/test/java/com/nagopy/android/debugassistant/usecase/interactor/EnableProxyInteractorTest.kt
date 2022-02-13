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

class EnableProxyInteractorTest {

    private lateinit var enableProxyInteractor: EnableProxyInteractor
    private lateinit var globalSettingsRepository: GlobalSettingsRepository
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    @Before
    fun setUp() {
        globalSettingsRepository = mockk(relaxed = true)
        userPreferencesRepository = mockk(relaxed = true)
        enableProxyInteractor =
            EnableProxyInteractor(globalSettingsRepository, userPreferencesRepository)
    }

    @Test
    fun enableProxy_putSuccess() {
        val host = "localhost"
        val port = "8080"

        every { globalSettingsRepository.putString(any(), any()) } returns true
        val ret = enableProxyInteractor.enableProxy(host, port)
        kotlin.test.assertTrue(ret)
        verifyAll {
            globalSettingsRepository.putString(Settings.Global.HTTP_PROXY, "$host:$port")
            userPreferencesRepository.proxyHost = host
            userPreferencesRepository.proxyPort = port
        }
    }

    @Test
    fun enableProxy_putError() {
        val host = "localhost"
        val port = "8080"

        every { globalSettingsRepository.putString(any(), any()) } returns false
        val ret2 = enableProxyInteractor.enableProxy(host, port)
        kotlin.test.assertFalse(ret2)
        verify { globalSettingsRepository.putString(Settings.Global.HTTP_PROXY, "$host:$port") }
        verify(inverse = true) {
            userPreferencesRepository.proxyHost = host
            userPreferencesRepository.proxyPort = port
        }
    }
}
