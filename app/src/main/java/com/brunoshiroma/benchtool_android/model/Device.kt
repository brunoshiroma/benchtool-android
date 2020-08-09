package com.brunoshiroma.benchtool_android.model

import androidx.databinding.ObservableField

class Device : BaseModel() {

    val cpu = ObservableField<String>()

    val osArch = ObservableField<String>()

    val osVersion = ObservableField<String>()

    val libDir = ObservableField<String>()

}