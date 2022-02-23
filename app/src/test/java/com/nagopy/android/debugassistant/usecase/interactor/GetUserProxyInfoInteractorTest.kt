package com.nagopy.android.debugassistant.usecase.interactor

import com.nagopy.android.debugassistant.repository.UserPreferencesRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class GetUserProxyInfoInteractorTest {
    private lateinit var getUserProxyInfoInteractor: GetUserProxyInfoInteractor
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    @Before
    fun setUp() {
        userPreferencesRepository = mockk(relaxed = true)
        getUserProxyInfoInteractor = GetUserProxyInfoInteractor(userPreferencesRepository)
    }

    @Test
    fun getUserProxyInfo() {

        every { userPreferencesRepository.proxyHost } returns "host"
        every { userPreferencesRepository.proxyPort } returns "port"
        val result = getUserProxyInfoInteractor.getUserProxyInfo()
        kotlin.test.assertEquals("host", result.host)
        kotlin.test.assertEquals("port", result.port)
    }
}
