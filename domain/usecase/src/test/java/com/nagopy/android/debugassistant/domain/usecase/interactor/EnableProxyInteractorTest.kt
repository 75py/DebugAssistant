package com.nagopy.android.debugassistant.domain.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.data.repository.GlobalSettingsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class EnableProxyInteractorTest {

    private lateinit var enableProxyInteractor: EnableProxyInteractor
    private lateinit var globalSettingsRepository: GlobalSettingsRepository

    @Before
    fun setUp() {
        globalSettingsRepository = mockk(relaxed = true)
        enableProxyInteractor =
            EnableProxyInteractor(globalSettingsRepository)
    }

    @Test
    fun enableProxy_putSuccess() {
        val host = "localhost"
        val port = "8080"

        every { globalSettingsRepository.putString(any(), any()) } returns true
        val ret = enableProxyInteractor.enableProxy(host, port)
        kotlin.test.assertTrue(ret)
        verify {
            globalSettingsRepository.putString(Settings.Global.HTTP_PROXY, "$host:$port")
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
    }
}
