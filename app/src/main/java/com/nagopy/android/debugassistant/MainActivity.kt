package com.nagopy.android.debugassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nagopy.android.debugassistant.ui.theme.DebugAssistantTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DebugAssistantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(Modifier.padding(16.dp)) {
                        val isPermissionGranted = mainViewModel.isPermissionGranted.collectAsState()
                        if (!isPermissionGranted.value) {
                            PermissionErrorMessage(onAdbCommandClicked = {
                                mainViewModel.onAdbCommandClicked(this@MainActivity)
                            })
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val proxyHost = mainViewModel.proxyHostFlow.collectAsState()
                        ProxyHost(
                            enabled = isPermissionGranted.value,
                            value = proxyHost.value,
                            onValueChanged = {
                                mainViewModel.proxyHostFlow.value = it
                            })

                        Spacer(modifier = Modifier.height(8.dp))

                        val proxyPort = mainViewModel.proxyPortFlow.collectAsState()
                        ProxyPort(
                            enabled = isPermissionGranted.value,
                            value = proxyPort.value,
                            onValueChanged = {
                                mainViewModel.proxyPortFlow.value = it
                            })

                        Spacer(modifier = Modifier.height(16.dp))

                        val isProxyEnabled = mainViewModel.isProxyEnabled.collectAsState()
                        ProxyToggleSwitch(
                            enabled = isPermissionGranted.value && proxyHost.value.isNotEmpty() && proxyPort.value.isNotEmpty(),
                            checked = isProxyEnabled.value,
                        ) {
                            mainViewModel.onProxySwitchClicked(it)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        val isAdbWifiEnabled = mainViewModel.isAdbWifiEnabled.collectAsState()
                        AdbWifiSwitch(
                            enabled = isPermissionGranted.value,
                            checked = isAdbWifiEnabled.value,
                        ) {
                            mainViewModel.onAdbWifiSwitchClicked(it)
                        }
                    }
                }
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
        Column(Modifier.padding(16.dp)) {
            PermissionErrorMessage {}
            Spacer(modifier = Modifier.height(16.dp))
            ProxyHost(true, "test") {}
            Spacer(modifier = Modifier.height(8.dp))
            ProxyPort(true, "9999") {}

            Spacer(modifier = Modifier.height(16.dp))
            ProxyToggleSwitch(enabled = true, checked = true) {}
        }
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

    Row(Modifier
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
fun AdbWifiSwitch(enabled: Boolean, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val alpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled

    Row(Modifier
        .toggleable(
            enabled = enabled,
            value = checked,
            role = Role.Switch,
            onValueChange = { onCheckedChange(it) }
        )
        .padding(16.dp)
        .fillMaxWidth()
    ) {
        Text(text = "Adb over Wifi", modifier = Modifier.alpha(alpha))
        Spacer(modifier = Modifier.width(8.dp))
        Switch(enabled = enabled, checked = checked, onCheckedChange = null)
    }
}
