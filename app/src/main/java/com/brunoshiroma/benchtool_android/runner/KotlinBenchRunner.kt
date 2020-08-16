package com.brunoshiroma.benchtool_android.runner

import com.brunoshiroma.benchtool.bench.*
import java.math.BigInteger
import java.util.*

/*
Kotlin Runner, can run any JVM language loaded by ART VM of android
 */
class KotlinBenchRunner : BenchRunner {

    override fun run(
        platform: String,
        type: String,
        iteration: Int,
        repeat: Int,
        expectedResult: String
    ): Triple<Int, BigInteger, String?> {

        return when (platform) {
            "java" -> {
                executeJava(type, iteration, repeat, expectedResult)
            }
            else -> Triple(0, BigInteger.ZERO, "unknown bench $platform")
        }
    }


    fun executeJava(
        type: String,
        iteration: Int,
        repeat: Int,
        expectedResult: String
    ): Triple<Int, BigInteger, String?> {
        var benchType = BenchType.fromIntValue(type.toInt())
        var bench: Bench? = null
        var nElementCount: String? = null

        val results: MutableList<BenchResult<BigInteger>> = ArrayList()

        bench = when (benchType) {
            BenchType.Recursive -> SimpleFibonacciRecursiveBench()
            BenchType.Loop -> SimpleFibonacciLoopBench()
        }

        for (i in 0 until repeat) {
            val result = bench!!.run(iteration.toString())
            results.add(result as BenchResult<BigInteger>)
        }


        val totalRunningMs = results.map { t -> t.runningMilliseconds }.sum()

        val averageMs: Int = (totalRunningMs / repeat).toInt()

        val rawResult: BigInteger = results[0].result()
        val resultsOk = results.map { m -> m.result() == rawResult }.toBooleanArray()
            .indexOfFirst { false } <= 0

        if (!resultsOk) {
            return Triple(0, BigInteger.ZERO, "All results are not ok =,(")
        }

        return Triple(averageMs, rawResult, null)
    }

}