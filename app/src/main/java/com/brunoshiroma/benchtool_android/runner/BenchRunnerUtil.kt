package com.brunoshiroma.benchtool_android.runner

import android.content.Context
import java.math.BigInteger

object BenchRunnerUtil : BenchRunner{

    private lateinit var context: Context

    private val kotlinBenchRunner = KotlinBenchRunner()

    private val nativeRunner = NativeRunner()

    fun setup(context: Context){
        this.context = context
        nativeRunner.setup(context.applicationInfo.nativeLibraryDir)
    }

    override fun run(
        platform: String,
        type: String,
        iteration: Int,
        repeat: Int,
        expectedResult: String
    ): Triple<Int, BigInteger, String?> {

        return when(platform){
            "java" -> kotlinBenchRunner.run(platform, type, iteration, repeat, expectedResult)
            else -> nativeRunner.run(platform, type, iteration, repeat, expectedResult)
        }

    }


}