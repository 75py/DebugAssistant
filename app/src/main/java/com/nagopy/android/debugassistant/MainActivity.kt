package com.nagopy.android.debugassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagopy.android.debugassistant.ui.theme.DebugAssistantTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DebugAssistantTheme {
                Scaffold(topBar = {
                    AppBar {
                        mainViewModel.onLicensesMenuClicked(this)
                    }
                }) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            val isPermissionGranted = mainViewModel.isPermissionGranted.collectAsState()
                            if (!isPermissionGranted.value) {
                                PermissionErrorMessage(onAdbCommandClicked = {
                                    mainViewModel.onAdbCommandClicked()
                                })
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            val proxyHost = mainViewModel.proxyHostFlow.collectAsState()
                            val proxyPort = mainViewModel.proxyPortFlow.collectAsState()
                            val isProxyEnabled = mainViewModel.isProxyEnabled.collectAsState()
                            ProxySection(
                                isPermissionGranted = isPermissionGranted.value,
                                proxyHost = proxyHost.value,
                                onProxyHostChanged = {
                                    mainViewModel.proxyHostFlow.value = it
                                },
                                proxyPort = proxyPort.value,
                                onProxyPortChanged = {
                                    mainViewModel.proxyPortFlow.value = it
                                },
                                isProxyEnabled = isProxyEnabled.value,
                                onProxySwitchClicked = {
                                    mainViewModel.onProxySwitchClicked(it)
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            val isAdbEnabled = mainViewModel.isAdbEnabled.collectAsState()
                            AdbSwitch(
                                enabled = isPermissionGranted.value,
                                checked = isAdbEnabled.value,
                            ) {
                                mainViewModel.onAdbSwitchClicked(it)
                            }
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
        Scaffold(
            topBar = { AppBar { } },
            content = {
                Column(Modifier.padding(16.dp)) {
                    PermissionErrorMessage {}
                    Spacer(modifier = Modifier.height(16.dp))

                    ProxySection(
                        isPermissionGranted = true,
                        proxyHost = "localhost",
                        onProxyHostChanged = {},
                        proxyPort = "8888",
                        onProxyPortChanged = {},
                        isProxyEnabled = true,
                        onProxySwitchClicked = {}
                    )
                }
            }
        )
    }
}

@Composable
fun AppBar(licensesClicked: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.MoreVert, "Menu")
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(onClick = {
                    showMenu = false
                    licensesClicked()
                }) {
                    Text(stringResource(id = R.string.licenses))
                }
            }
        },
    )
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

        Spacer(modifier = Modifier.height(8.dp))

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
