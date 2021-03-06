package com.brunoshiroma.benchtool_android.benchtool_rust

import android.util.Log
import androidx.annotation.Keep
import com.brunoshiroma.benchtool_android.BenchtoolApplication
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import dalvik.system.BaseDexClassLoader

@Keep
class Binder {

    interface CLibrary : Library {
        fun benchtool_rust_call(iteration: Int, repeat: Int, bench_type: Int) : String

        companion object {
            val INSTANCE = Native.load("benchtoolrust",
                CLibrary::class.java
            ) as CLibrary
        }
    }

    fun execute(iteration: Int, repeat: Int, bench_type: Int) : String {
        Log.d("RUST_BINDER", "STARTING")

        val binaryName =
            (BenchtoolApplication.app.value.classLoader as BaseDexClassLoader).findLibrary("benchtoolrust")

        val libPath = binaryName.substring(0, binaryName.lastIndexOf("/"))

        NativeLibrary.addSearchPath("benchtoolrust", libPath)

        val instance = Native.load("benchtoolrust",
            CLibrary::class.java
        ) as CLibrary

        val result = instance.benchtool_rust_call(iteration, repeat, bench_type)
        Log.d("RUST_BINDER_RESULT", result)
        return result
    }

}