package com.brunoshiroma.benchtool_android.model

import android.widget.Toast
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.brunoshiroma.benchtool_android.BenchtoolApplication
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.splitinstall.SplitInstallRequest

class BenchConfig : BaseModel() {

    val platform = ObservableField<String>()

    val type = ObservableField<String>()

    val iteration = ObservableField<String>()

    val repeat = ObservableField<String>()

    val acceptLargeIteration = ObservableField<Boolean>()

    val downloadOK = ObservableField<Boolean>()

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
                                Toast.makeText(BenchtoolApplication.app.value, "Download OK", Toast.LENGTH_SHORT).show()
                                downloadOK.set(true)
                            }
                        } else{
                            downloadOK.set(true)
                        }
                    }
                    else -> {
                        downloadOK.set(true)
                    }
                }
            }
        })

    }

}