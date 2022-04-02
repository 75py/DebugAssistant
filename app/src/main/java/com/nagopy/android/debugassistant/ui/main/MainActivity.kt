package com.nagopy.android.debugassistant.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagopy.android.debugassistant.BuildConfig
import com.nagopy.android.debugassistant.R
import com.nagopy.android.debugassistant.ui.theme.DebugAssistantTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DebugAssistantTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(id = R.string.app_name)) },
                        )
                    },
                    content = {
                        val state = mainViewModel.viewModelState.collectAsState().value
                        MainScreen(
                            proxyHost = state.proxyHost,
                            proxyPort = state.proxyPort,
                            isPermissionGranted = state.isPermissionGranted,
                            isProxyEnabled = state.isProxyEnabled,
                            isAdbEnabled = state.isAdbEnabled,
                            onAdbCommandClicked = { mainViewModel.onAdbCommandClicked() },
                            onProxyHostChanged = { mainViewModel.onProxyHostChanged(it) },
                            onProxyPortChanged = { mainViewModel.onProxyPortChanged(it) },
                            onProxySwitchClicked = { mainViewModel.onProxySwitchClicked(it) },
                            onAdbSwitchClicked = { mainViewModel.onAdbSwitchClicked(it) },
                            onHowToUseButtonClicked = { mainViewModel.onHowToUseButtonClicked() },
                            onLicensesButtonClicked = { mainViewModel.onLicensesButtonClicked() },
                        )
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.updateStatus()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    DebugAssistantTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.app_name)) },
                )
            },
            content = {
                MainScreen(
                    proxyHost = "host",
                    proxyPort = "port",
                    isPermissionGranted = false,
                    isProxyEnabled = true,
                    isAdbEnabled = true,
                    onAdbCommandClicked = {},
                    onProxyHostChanged = {},
                    onProxyPortChanged = {},
                    onProxySwitchClicked = {},
                    onAdbSwitchClicked = {},
                    onHowToUseButtonClicked = {},
                    onLicensesButtonClicked = {},
                )
            }
        )
    }
}

@Composable
fun MainScreen(
    proxyHost: String,
    proxyPort: String,
    isPermissionGranted: Boolean,
    isProxyEnabled: Boolean,
    isAdbEnabled: Boolean,
    onAdbCommandClicked: () -> Unit,
    onProxyHostChanged: (String) -> Unit,
    onProxyPortChanged: (String) -> Unit,
    onProxySwitchClicked: (Boolean) -> Unit,
    onAdbSwitchClicked: (Boolean) -> Unit,
    onHowToUseButtonClicked: () -> Unit,
    onLicensesButtonClicked: () -> Unit,
) {
    Column(
        Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (!isPermissionGranted) {
            PermissionErrorMessage(onAdbCommandClicked = onAdbCommandClicked)

            Spacer(modifier = Modifier.height(16.dp))
        }

        ProxySection(
            isPermissionGranted = isPermissionGranted,
            proxyHost = proxyHost,
            onProxyHostChanged = onProxyHostChanged,
            proxyPort = proxyPort,
            onProxyPortChanged = onProxyPortChanged,
            isProxyEnabled = isProxyEnabled,
            onProxySwitchClicked = onProxySwitchClicked,
        )

        Spacer(modifier = Modifier.height(16.dp))

        AdbSection(
            isPermissionGranted = isPermissionGranted,
            isAdbEnabled = isAdbEnabled,
            onAdbSwitchClicked = onAdbSwitchClicked
        )

        Spacer(modifier = Modifier.height(16.dp))

        AboutSection(
            onHowToUseButtonClicked = onHowToUseButtonClicked,
            onLicensesButtonClicked = onLicensesButtonClicked,
        )
    }
}

@Composable
fun PermissionErrorMessage(onAdbCommandClicked: () -> Unit) {
    Column {
        Text(
            text = "WRITE_SECURE_SETTINGS is not granted.\nRun the following command:",
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onAdbCommandClicked() }) {
            Text(
                text = "adb shell pm grant ${BuildConfig.APPLICATION_ID} android.permission.WRITE_SECURE_SETTINGS",
            )
        }
    }
}

@Composable
fun ProxySection(
    isPermissionGranted: Boolean,
    proxyHost: String,
    onProxyHostChanged: (String) -> Unit,
    proxyPort: String,
    onProxyPortChanged: (String) -> Unit,
    isProxyEnabled: Boolean,
    onProxySwitchClicked: (Boolean) -> Unit,
) {
    Column {
        Text(
            "Settings.Global.HTTP_PROXY", fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(3.dp))
        Divider()

        Spacer(modifier = Modifier.height(8.dp))

        ProxyHost(
            enabled = isPermissionGranted,
            value = proxyHost,
            onValueChanged = {
                onProxyHostChanged(it)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ProxyPort(
            enabled = isPermissionGranted,
            value = proxyPort,
            onValueChanged = {
                onProxyPortChanged(it)
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        ProxyToggleSwitch(
            enabled = isPermissionGranted && proxyHost.isNotEmpty() && proxyPort.isNotEmpty(),
            checked = isProxyEnabled,
        ) {
            onProxySwitchClicked(it)
        }
    }
}

@Composable
fun ProxyHost(enabled: Boolean, value: String, onValueChanged: (String) -> Unit) {
    OutlinedTextField(
        enabled = enabled,
        value = value,
        onValueChange = { onValueChanged(it) },
        label = { Text("Proxy Host") },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Uri
        )
    )
}

@Composable
fun ProxyPort(enabled: Boolean, value: String, onValueChanged: (String) -> Unit) {
    OutlinedTextField(
        enabled = enabled,
        value = value,
        onValueChange = { onValueChanged(it) },
        label = { Text("Proxy Port") },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
fun ProxyToggleSwitch(enabled: Boolean, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val alpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled

    Row(
        modifier = Modifier
            .toggleable(
                enabled = enabled,
                value = checked,
                role = Role.Switch,
                onValueChange = { onCheckedChange(it) }
            )
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = "Use Proxy", modifier = Modifier.alpha(alpha))
        Spacer(modifier = Modifier.width(8.dp))
        Switch(enabled = enabled, checked = checked, onCheckedChange = null)
    }
}

@Composable
fun AdbSection(
    isPermissionGranted: Boolean,
    isAdbEnabled: Boolean,
    onAdbSwitchClicked: (Boolean) -> Unit,
) {
    Column {
        Text(
            "Settings.Global.ADB_ENABLED", fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(3.dp))
        Divider()

        Spacer(modifier = Modifier.height(8.dp))

        AdbSwitch(enabled = isPermissionGranted, checked = isAdbEnabled, onCheckedChange = onAdbSwitchClicked)
    }
}

@Composable
fun AdbSwitch(enabled: Boolean, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val alpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled

    Row(
        Modifier
            .toggleable(
                enabled = enabled,
                value = checked,
                role = Role.Switch,
                onValueChange = { onCheckedChange(it) }
            )
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = "Adb", modifier = Modifier.alpha(alpha))
        Spacer(modifier = Modifier.width(8.dp))
        Switch(enabled = enabled, checked = checked, onCheckedChange = null)
    }
}

@Composable
fun AboutSection(
    onHowToUseButtonClicked: () -> Unit,
    onLicensesButtonClicked: () -> Unit,
) {
    Column {
        Text(
            "About", fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(3.dp))
        Divider()

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onHowToUseButtonClicked) {
            Text("How to use")
        }
        Spacer(modifier = Modifier.height(4.dp))
        Button(onClick = onLicensesButtonClicked) {
            Text("Open source licenses")
        }
    }
}
