package com.nagopy.android.debugassistant.domain.model

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProxyInfoTest {

    @Test
    fun isAvailable() {
        assertFalse(ProxyInfo("", "").isAvailable())
        assertFalse(ProxyInfo("localhost", "").isAvailable())
        assertFalse(ProxyInfo("", "8888").isAvailable())
        assertTrue(ProxyInfo("localhost", "8888").isAvailable())
    }
}
