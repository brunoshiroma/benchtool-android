package com.brunoshiroma.benchtool_android.model

import androidx.databinding.ObservableField
import com.brunoshiroma.benchtool_android.runner.BenchRunnerUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigInteger

class BenchResult : BaseModel() {

    val result = ObservableField<BigInteger>()
    val platform = ObservableField<String>()
    val executionTime = ObservableField<Int>()
    val executing = ObservableField<Boolean>()

    val config = ObservableField<BenchConfig>()

    val errorMessage = ObservableField<String>()

    private fun doWork() {
        val benchResult = BenchRunnerUtil.run(
            config.get()?.platform?.get() ?: "go",
            config.get()?.type?.get() ?: "1",
            config.get()?.iteration?.get()?.toInt() ?: 1000000,
            config.get()?.repeat?.get()?.toInt() ?: 5, "")

        result.set(benchResult.second)
        executionTime.set(benchResult.first)
        errorMessage.set(benchResult.third)

    }

    fun onExecute(){
        result.set(BigInteger.ZERO)
        executionTime.set(0)
        executing.set(true)

        GlobalScope.launch(Dispatchers.IO) {
            doWork()
            executing.set(false)
        }


    }

}