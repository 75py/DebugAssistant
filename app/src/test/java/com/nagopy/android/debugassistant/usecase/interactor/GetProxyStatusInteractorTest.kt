package com.nagopy.android.debugassistant.usecase.interactor

import android.provider.Settings
import com.nagopy.android.debugassistant.repository.GlobalSettingsRepository
import com.nagopy.android.debugassistant.repository.UserPreferencesRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GetProxyStatusInteractorTest {
    private lateinit var getProxyStatusInteractor: GetProxyStatusInteractor
    private lateinit var globalSettingsRepository: GlobalSettingsRepository
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    @Before
    fun setUp() {
        globalSettingsRepository = mockk(relaxed = true)
        userPreferencesRepository = mockk(relaxed = true)
        getProxyStatusInteractor =
            GetProxyStatusInteractor(globalSettingsRepository)
    }

    @Test
    fun isProxyEnabled() {
        every { globalSettingsRepository.getString(Settings.Global.HTTP_PROXY) } returns null
        kotlin.test.assertFalse(getProxyStatusInteractor.isProxyEnabled())
        verify { globalSettingsRepository.getString(Settings.Global.HTTP_PROXY) }

        every { globalSettingsRepository.getString(Settings.Global.HTTP_PROXY) } returns ""
        kotlin.test.assertFalse(getProxyStatusInteractor.isProxyEnabled())
        verify { globalSettingsRepository.getString(Settings.Global.HTTP_PROXY) }

        every { globalSettingsRepository.getString(Settings.Global.HTTP_PROXY) } returns GlobalSettingsRepository.DISABLE_PROXY_VALUE
        kotlin.test.assertFalse(getProxyStatusInteractor.isProxyEnabled())
        verify { globalSettingsRepository.getString(Settings.Global.HTTP_PROXY) }

        every { globalSettingsRepository.getString(Settings.Global.HTTP_PROXY) } returns "localhost:8080"
        kotlin.test.assertTrue(getProxyStatusInteractor.isProxyEnabled())
        verify { globalSettingsRepository.getString(Settings.Global.HTTP_PROXY) }
    }
}