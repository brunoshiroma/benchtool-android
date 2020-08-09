package com.brunoshiroma.benchtool_android.model

import androidx.databinding.ObservableField

class BenchConfig : BaseModel() {

    val platform = ObservableField<String>()

    val type = ObservableField<String>()

    val iteration = ObservableField<String>()

    val repeat = ObservableField<String>()

}