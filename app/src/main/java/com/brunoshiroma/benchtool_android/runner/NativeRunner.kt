package com.brunoshiroma.benchtool_android.runner

import android.os.Build
import android.util.Log
import com.brunoshiroma.benchtool_android.BenchtoolApplication
import com.brunoshiroma.benchtool_android.MainActivity
import com.google.android.play.core.splitinstall.SplitInstallHelper
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import dalvik.system.BaseDexClassLoader
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigInteger

class NativeRunner : AbstractBenchRunner() {

    override fun doRun(
        platform: String,
        type: String,
        iteration: Int,
        repeat: Int,
        expectedResult: String
    ): Triple<Int, BigInteger, String?> {

        try {
            if ("go" == platform) {//shipped with base apk

                val golangBenchRunner = GolangBenchRunner()

                val result = golangBenchRunner.execute(iteration, repeat.toInt(), type.toInt())

                val parts = result.split(" ")

                return Triple(parts[0].toInt(), BigInteger(parts[1].trim(), 10), null)
            } else {//from play core
                SplitInstallHelper.updateAppInfo(BenchtoolApplication.app.value)
                SplitInstallHelper.loadLibrary(BenchtoolApplication.app.value, "benchtool${platform}")

                val className = "com.brunoshiroma.benchtool_android.benchtool_${platform}.Binder"

                val nativeBinderClass = BenchtoolApplication.app.value.classLoader.loadClass(className)
                val nativeBinderInstance = nativeBinderClass.newInstance()

                val nativeBinderMethod = nativeBinderClass.getMethod("execute", Int::class.java, Int::class.java, Int::class.java)
                val result : String =
                    nativeBinderMethod.invoke(nativeBinderInstance, iteration, repeat.toInt(), type.toInt()) as String

                //the result should be '1 123456'
                val parts = result.split(" ")
                val execTime : Int = parts[0].trim().toInt()
                val bigIntegerResult : BigInteger = BigInteger(parts[1].trim(), 10)

                return Triple(execTime, bigIntegerResult, null)
            }

        } catch (e: Exception) {

            val message : String = if(e.message != null){
                e.message!!
            } else {
                e.toString()
            }

            Log.e("NATIVE_EXEC", message)
            return Triple(0, BigInteger.ZERO, e.message)
        }
    }

    override fun getName(): String {
        return "NativeRunner"
    }

}