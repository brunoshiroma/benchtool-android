package com.brunoshiroma.benchtool_android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brunoshiroma.benchtool_android.model.BenchConfig
import com.brunoshiroma.benchtool_android.model.BenchResult
import com.brunoshiroma.benchtool_android.model.Device
import com.brunoshiroma.benchtool_android.ui.theme.BenchtoolTheme
import com.google.android.play.core.splitcompat.SplitCompat
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.NativeLibrary


class MainActivity : AppCompatActivity() {

    interface CLibrary : Library {
        fun benchtoolGoCall(iteration: Int, repeat: Int, bench_type: Int): String

        companion object {
            val INSTANCE = Native.load(
                "benchtool-go-lib-android-arm64",
                CLibrary::class.java
            ) as CLibrary
        }
    }

    private val benchResult: BenchResult by viewModels()
    private val device: Device by viewModels()
    private val config: BenchConfig by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            device.osArch.value = Build.SUPPORTED_ABIS.joinToString(", ")
        } else {
            @Suppress("DEPRECATION")
            device.osArch.value = Build.CPU_ABI
        }
        device.libDir.value = applicationInfo.nativeLibraryDir

        SplitCompat.installActivity(this)

        setContent {
            BenchtoolTheme {
                MainScreen(
                    benchResult = benchResult,
                    config = config,
                    device = device,
                    onAboutClick = {
                        startActivity(Intent(this, AboutActivity::class.java))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    benchResult: BenchResult,
    config: BenchConfig,
    device: Device,
    onAboutClick: () -> Unit
) {
    val result by benchResult.result.collectAsStateWithLifecycle()
    val executionTime by benchResult.executionTime.collectAsStateWithLifecycle()
    val executing by benchResult.executing.collectAsStateWithLifecycle()
    val errorMessage by benchResult.errorMessage.collectAsStateWithLifecycle()
    val showLargeIterationWarning by benchResult.showLargeIterationWarning.collectAsStateWithLifecycle()

    val platform by config.platform.collectAsStateWithLifecycle()
    val iteration by config.iteration.collectAsStateWithLifecycle()
    val repeat by config.repeat.collectAsStateWithLifecycle()
    val downloadOK by config.downloadOK.collectAsStateWithLifecycle()
    val downloaded by config.downloaded.collectAsStateWithLifecycle()
    val downloadSize by config.downloadSize.collectAsStateWithLifecycle()

    val osArch by device.osArch.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val largeIterationMsg = stringResource(R.string.bench_iteration_large_msg)
    val continueMsg = stringResource(R.string.bench_large_continue)

    LaunchedEffect(showLargeIterationWarning) {
        if (showLargeIterationWarning) {
            val snackResult = snackbarHostState.showSnackbar(
                message = largeIterationMsg,
                actionLabel = continueMsg,
                duration = SnackbarDuration.Short
            )
            if (snackResult == SnackbarResult.ActionPerformed) {
                config.acceptLargeIteration.value = true
                benchResult.onExecute(config)
            }
            benchResult.dismissLargeIterationWarning()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (!executing) {
                BottomAppBar(
                    actions = {
                        IconButton(onClick = onAboutClick) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = stringResource(R.string.bench_about)
                            )
                        }
                    },
                    floatingActionButton = {
                        Box(contentAlignment = Alignment.Center) {
                            FloatingActionButton(
                                onClick = { if (downloadOK) benchResult.onExecute(config) }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = stringResource(R.string.bench_execute)
                                )
                            }
                            if (downloaded > 0) {
                                CircularProgressIndicator(
                                    progress = {
                                        if (downloadSize > 0) downloaded.toFloat() / downloadSize.toFloat() else 0f
                                    },
                                    modifier = Modifier.size(56.dp)
                                )
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (executing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center)
                )
            }

            if (!errorMessage.isNullOrEmpty()) {
                Text(
                    text = errorMessage.orEmpty(),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 36.dp, top = 40.dp)
                )
            }

            Text(
                text = result.toString(10),
                modifier = Modifier.align(Alignment.Center),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${executionTime ?: 0}ms",
                    fontSize = 32.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    OutlinedTextField(
                        value = iteration,
                        onValueChange = { config.iteration.value = it },
                        label = { Text(stringResource(R.string.bench_iteration)) },
                        enabled = !executing,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = repeat,
                        onValueChange = { config.repeat.value = it },
                        label = { Text(stringResource(R.string.bench_repeat)) },
                        enabled = !executing,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    PlatformDropdown(
                        platform = platform,
                        onPlatformChange = { config.platform.value = it },
                        enabled = !executing,
                        modifier = Modifier.weight(1f)
                    )
                }
                Text(
                    text = osArch,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 5.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatformDropdown(
    platform: String,
    onPlatformChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val platforms = listOf("java", "go", "rust")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded && enabled,
        onExpandedChange = { if (enabled) expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = platform,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.bench_platform)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded && enabled) },
            enabled = enabled,
            singleLine = true,
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { expanded = false }
        ) {
            platforms.forEach { p ->
                DropdownMenuItem(
                    text = { Text(p) },
                    onClick = {
                        onPlatformChange(p)
                        expanded = false
                    }
                )
            }
        }
    }
}
