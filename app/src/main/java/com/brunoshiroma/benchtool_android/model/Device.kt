package com.brunoshiroma.benchtool_android.model

import kotlinx.coroutines.flow.MutableStateFlow

class Device : BaseModel() {

    val cpu = MutableStateFlow("")

    val osArch = MutableStateFlow("")

    val osVersion = MutableStateFlow("")

    val libDir = MutableStateFlow("")

}