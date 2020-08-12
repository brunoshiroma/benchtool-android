package com.brunoshiroma.benchtool_android

import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.math.BigInteger

class BenchRunner{

    companion object{

        private lateinit var nativeLibDir: String

        fun setup(nativeLibDir: String) {
            this.nativeLibDir = nativeLibDir
        }

        fun run(platform: String, type: String, iteration: Int, repeat: Int, expectedResult: String) : Triple<Int, BigInteger, String?>{

            try{

                var binaryName = File(this.nativeLibDir).list()?.get(0);

                val nativeApp = Runtime
                    .getRuntime()
                    .exec(arrayOf("${this.nativeLibDir}/$binaryName", type, iteration.toString(), repeat.toString()))

                val reader = BufferedReader(InputStreamReader(nativeApp.inputStream))
                var read: Int = 0
                val buffer = CharArray(4096)
                val output = StringBuffer()
                val errorOutput = StringBuffer()
                while (reader.read(buffer).also({ read = it }) > 0) {
                    output.append(buffer, 0, read)
                }
                reader.close()

                val readerError = BufferedReader(InputStreamReader(nativeApp.errorStream))
                while (readerError.read(buffer).also({ read = it }) > 0) {
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

            }catch (e: Exception){
                Log.e("NATIVE_EXEC", e.message, e)
                return Triple(0, BigInteger.ZERO, e.message)
            }
        }
    }

}