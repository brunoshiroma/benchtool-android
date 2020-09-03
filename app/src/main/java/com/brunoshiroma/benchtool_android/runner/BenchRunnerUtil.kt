package com.brunoshiroma.benchtool_android.runner

import androidx.test.espresso.IdlingResource
import java.math.BigInteger

object BenchRunnerUtil : BenchRunner{

    private val kotlinBenchRunner = KotlinBenchRunner()

    private val nativeRunner = NativeRunner()

    fun getIdle(): Array<IdlingResource>{
        return arrayOf(kotlinBenchRunner,nativeRunner)
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