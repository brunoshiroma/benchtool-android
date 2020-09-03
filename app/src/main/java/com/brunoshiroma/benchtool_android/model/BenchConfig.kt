package com.brunoshiroma.benchtool_android.model

import android.widget.Toast
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.brunoshiroma.benchtool_android.BenchtoolApplication
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

class BenchConfig : BaseModel() {

    val platform = ObservableField<String>()

    val type = ObservableField<String>()

    val iteration = ObservableField<String>()

    val repeat = ObservableField<String>()

    val acceptLargeIteration = ObservableField<Boolean>()

    val downloadOK = ObservableField<Boolean>()

    val downloadSize = ObservableField<Int>()

    val downloaded = ObservableField<Int>()

    init{
        platform.addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable?, i: Int) {
                when(platform.get()){
                    "rust" -> {
                        downloadOK.set(false)
                        if(!BenchtoolApplication.manager.installedModules.contains("benchtool_rust")){

                            val installRequest = SplitInstallRequest
                                .newBuilder()
                                .addModule("benchtool_rust")
                                .build()

                            val installTask = BenchtoolApplication.manager.startInstall(installRequest)

                            Toast.makeText(BenchtoolApplication.app.value, "Download benchtool_rust", Toast.LENGTH_SHORT).show()

                            installTask.addOnCompleteListener {
                                if(installTask.isSuccessful){

                                    BenchtoolApplication.manager.registerListener{
                                        when(it.status()){
                                            SplitInstallSessionStatus.INSTALLED -> {
                                                downloaded.set(0)
                                                downloadSize.set(0)
                                                downloadOK.set(true)
                                                Toast
                                                    .makeText(BenchtoolApplication.app.value, "Download OK", Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                            SplitInstallSessionStatus.FAILED -> {
                                                downloaded.set(0)
                                                downloadSize.set(0)
                                                downloadOK.set(false)
                                                Toast
                                                    .makeText(BenchtoolApplication.app.value, "Erro ${it.errorCode()}", Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                            SplitInstallSessionStatus.DOWNLOADING -> {
                                                downloadSize.set(it.totalBytesToDownload().toInt())
                                                downloaded.set(it.bytesDownloaded().toInt())
                                            }
                                        }

                                    }

                                } else {
                                    val message = installTask.exception?.message
                                    Toast.makeText(BenchtoolApplication.app.value, "ERROR $message", Toast.LENGTH_SHORT).show()
                                    downloadOK.set(false)
                                }

                            }
                        } else{
                            downloadOK.set(true)
                            downloaded.set(0)
                            downloadSize.set(0)
                        }
                    }
                    else -> {
                        downloadOK.set(true)
                        downloaded.set(0)
                        downloadSize.set(0)
                    }
                }
            }
        })

    }

}