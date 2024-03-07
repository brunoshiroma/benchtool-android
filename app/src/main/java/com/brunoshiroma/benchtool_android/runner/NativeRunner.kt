package com.brunoshiroma.benchtool_android.runner

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

                val binaryName2 =
                    (BenchtoolApplication.app.value.classLoader as BaseDexClassLoader).findLibrary("benchtool-go-lib-android-arm64")

                val libPath = binaryName2.substring(0, binaryName2.lastIndexOf("/"))

                NativeLibrary.addSearchPath("benchtool-go-lib-android-arm64", libPath)


                val instance = Native.load("benchtool-go-lib-android-arm64",
                    MainActivity.CLibrary::class.java
                ) as MainActivity.CLibrary

                val result = instance.benchtoolGoCall(1, 2, 3)

                var binaryName =
                    (BenchtoolApplication.app.value.classLoader as BaseDexClassLoader).findLibrary("benchtool-${platform}")

                val nativeApp = Runtime
                    .getRuntime()
                    .exec(
                        arrayOf(
                            binaryName,
                            type,
                            iteration.toString(),
                            repeat.toString()
                        )
                    )

                val reader = BufferedReader(InputStreamReader(nativeApp.inputStream))
                var read = 0
                val buffer = CharArray(4096)
                val output = StringBuffer()
                val errorOutput = StringBuffer()
                while (reader.read(buffer).also { read = it } > 0) {
                    output.append(buffer, 0, read)
                }
                reader.close()

                val readerError = BufferedReader(InputStreamReader(nativeApp.errorStream))
                while (readerError.read(buffer).also { read = it } > 0) {
                    errorOutput.append(buffer, 0, read)
                }
                readerError.close()

                // Waits for the command to finish.

                // Waits for the command to finish.
                nativeApp.waitFor()

                val nativeOutput = output.toString()
                Log.d("NATIVE_OUT", nativeOutput)

                val parts = nativeOutput.split(" ")

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