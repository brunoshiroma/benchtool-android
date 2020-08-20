package com.brunoshiroma.benchtool_android.runner

import androidx.test.espresso.IdlingResource
import java.math.BigInteger
import java.util.concurrent.atomic.AtomicBoolean

open abstract  class AbstractBenchRunner : BenchRunner, IdlingResource {

    private var isIdle : AtomicBoolean = AtomicBoolean(true)

    private var idlingCallBack : IdlingResource.ResourceCallback? = null

    override fun run(
        platform: String,
        type: String,
        iteration: Int,
        repeat: Int,
        expectedResult: String
    ): Triple<Int, BigInteger, String?> {
        isIdle.set(false)
        val result = doRun(platform, type, iteration, repeat, expectedResult)
        isIdle.set(true)
        idlingCallBack?.onTransitionToIdle()
        return result
    }

    abstract fun doRun(platform: String,
    type: String,
    iteration: Int,
    repeat: Int,
    expectedResult: String
    ): Triple<Int, BigInteger, String?>

    override fun isIdleNow(): Boolean {
        return isIdle.get()
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        idlingCallBack = callback
    }

}