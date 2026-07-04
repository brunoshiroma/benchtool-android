package com.brunoshiroma.benchtool_android.model

import android.widget.Toast
import com.brunoshiroma.benchtool_android.BenchtoolApplication
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BenchConfig : BaseModel() {

    val platform = MutableStateFlow("go")

    val type = MutableStateFlow("1")

    val iteration = MutableStateFlow("100000")

    val repeat = MutableStateFlow("10")

    val acceptLargeIteration = MutableStateFlow(false)

    val downloadOK = MutableStateFlow(true)

    val downloadSize = MutableStateFlow(0)

    val downloaded = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            platform.collect { p ->
                when (p) {
                    "rust" -> {
                        downloadOK.value = false
                        if (!BenchtoolApplication.manager.installedModules.contains("benchtool_rust")) {

                            val installRequest = SplitInstallRequest
                                .newBuilder()
                                .addModule("benchtool_rust")
                                .build()

                            val installTask = BenchtoolApplication.manager.startInstall(installRequest)

                            Toast.makeText(BenchtoolApplication.app.value, "Download benchtool_rust", Toast.LENGTH_SHORT).show()

                            installTask.addOnCompleteListener {
                                if (installTask.isSuccessful) {

                                    BenchtoolApplication.manager.registerListener {
                                        when (it.status()) {
                                            SplitInstallSessionStatus.INSTALLED -> {
                                                downloaded.value = 0
                                                downloadSize.value = 0
                                                downloadOK.value = true
                                                Toast
                                                    .makeText(BenchtoolApplication.app.value, "Download OK", Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                            SplitInstallSessionStatus.FAILED -> {
                                                downloaded.value = 0
                                                downloadSize.value = 0
                                                downloadOK.value = false
                                                Toast
                                                    .makeText(BenchtoolApplication.app.value, "Erro ${it.errorCode()}", Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                            SplitInstallSessionStatus.DOWNLOADING -> {
                                                downloadSize.value = it.totalBytesToDownload().toInt()
                                                downloaded.value = it.bytesDownloaded().toInt()
                                            }
                                        }
                                    }

                                } else {
                                    val message = installTask.exception?.message
                                    Toast.makeText(BenchtoolApplication.app.value, "ERROR $message", Toast.LENGTH_SHORT).show()
                                    downloadOK.value = false
                                }
                            }
                        } else {
                            downloadOK.value = true
                            downloaded.value = 0
                            downloadSize.value = 0
                        }
                    }
                    else -> {
                        downloadOK.value = true
                        downloaded.value = 0
                        downloadSize.value = 0
                    }
                }
            }
        }
    }

}