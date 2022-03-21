package com.nagopy.android.debugassistant.usecase.interactor

import com.nagopy.android.debugassistant.domain.ProxyInfo
import com.nagopy.android.debugassistant.repository.UserPreferencesRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class PutUserProxyInfoInteractorTest {
    private lateinit var putUserProxyInfoInteractor: PutUserProxyInfoInteractor
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    @Before
    fun setUp() {
        userPreferencesRepository = mockk(relaxed = true)
        putUserProxyInfoInteractor = PutUserProxyInfoInteractor(userPreferencesRepository)
    }

    @Test
    fun getUserProxyInfo() {
        val proxyInfo = ProxyInfo("host", "port")
        putUserProxyInfoInteractor.putUserProxyInfo(proxyInfo)
        verify {
            userPreferencesRepository.proxyHost = "host"
            userPreferencesRepository.proxyPort = "port"
        }
    }
}
