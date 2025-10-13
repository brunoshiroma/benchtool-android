package com.brunoshiroma.benchtool_android.runner

import android.util.Log
import androidx.annotation.Keep
import com.brunoshiroma.benchtool_android.BenchtoolApplication
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import dalvik.system.BaseDexClassLoader

@Keep
class GolangBenchRunner {

    interface CLibrary : Library {
        fun benchtoolGoCall(iteration: Int, repeat: Int, bench_type: Int) : String

        companion object {
            val INSTANCE = Native.load("benchtool-go",
                CLibrary::class.java
            ) as CLibrary
        }
    }

    fun execute(iteration: Int, repeat: Int, bench_type: Int) : String {
        Log.d("Golang-binder", "STARTING")

        val binaryName =
            (BenchtoolApplication.app.value.classLoader as BaseDexClassLoader).findLibrary("benchtool-go")

        val libPath = binaryName.substring(0, binaryName.lastIndexOf("/"))

        NativeLibrary.addSearchPath("benchtool-go", libPath)
        val instance = Native.load("benchtool-go",
            CLibrary::class.java
        ) as CLibrary

        val result = instance.benchtoolGoCall(iteration, repeat, bench_type)
        Log.d("Golang-binder", result)
        return result
    }

}