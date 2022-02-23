package com.nagopy.android.debugassistant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.input.ImeAction
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class MainActivityKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun permissionErrorMessage() {
        val onClickAction: () -> Unit = mockk(relaxed = true)
        composeTestRule.setContent {
            PermissionErrorMessage(onClickAction)
        }
        composeTestRule.onNode(hasClickAction())
            .performClick()
        verify {
            onClickAction()
        }
    }

    @Test
    fun proxyHost() {
        composeTestRule.setContent {
            Column {
                Row(modifier = Modifier.testTag("enabled")) {
                    ProxyHost(enabled = true, value = "value1", onValueChanged = {})
                }
                Row(modifier = Modifier.testTag("disabled")) {
                    ProxyHost(enabled = false, value = "value2", onValueChanged = {})
                }
            }
        }
        composeTestRule.onNodeWithTag("enabled")
            .onChildren()
            .filterToOne(hasImeAction(ImeAction.Default))
            .assertIsEnabled()
            .assertTextEquals("Proxy Host", includeEditableText = false)
        composeTestRule.onNodeWithTag("disabled")
            .onChildren()
            .filterToOne(hasImeAction(ImeAction.Default))
            .assertIsNotEnabled()
            .assertTextEquals("Proxy Host", includeEditableText = false)
    }

    @Test
    fun proxyPort() {
        composeTestRule.setContent {
            Column {
                Row(modifier = Modifier.testTag("enabled")) {
                    ProxyPort(enabled = true, value = "value1", onValueChanged = {})
                }
                Row(modifier = Modifier.testTag("disabled")) {
                    ProxyPort(enabled = false, value = "value2", onValueChanged = {})
                }
            }
        }
        composeTestRule.onNodeWithTag("enabled")
            .onChildren()
            .filterToOne(hasImeAction(ImeAction.Default))
            .assertIsEnabled()
            .assertTextEquals("Proxy Port", includeEditableText = false)
        composeTestRule.onNodeWithTag("disabled")
            .onChildren()
            .filterToOne(hasImeAction(ImeAction.Default))
            .assertIsNotEnabled()
            .assertTextEquals("Proxy Port", includeEditableText = false)
    }

    @Test
    fun proxyToggleSwitch() {
        var i = 0
        composeTestRule.setContent {
            ProxyToggleSwitch(enabled = true, checked = true, onCheckedChange = { i++ })
        }

        val clickable = composeTestRule.onNode(hasClickAction())
        clickable
            .assertIsEnabled()
            .assertIsOn()
            .assertIsDisplayed()

        clickable.performClick()
        assertEquals(1, i)
    }

    @Test
    fun adbSwitch() {
        var i = 0
        composeTestRule.setContent {
            AdbSwitch(enabled = true, checked = true, onCheckedChange = { i++ })
        }

        val clickable = composeTestRule.onNode(hasClickAction())
        clickable
            .assertIsEnabled()
            .assertIsOn()
            .assertIsDisplayed()

        clickable.performClick()
        assertEquals(1, i)
    }

    @Test
    fun adbSwitch_disabled() {
        composeTestRule.setContent {
            AdbSwitch(enabled = false, checked = false, onCheckedChange = { })
        }
        composeTestRule.onNode(hasClickAction())
            .assertIsNotEnabled()
            .assertIsOff()
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Adb")
            .assertIsDisplayed()
    }
}
