package com.brunoshiroma.benchtool_android.model

import androidx.lifecycle.viewModelScope
import com.brunoshiroma.benchtool_android.runner.BenchRunnerUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.math.BigInteger

class BenchResult : BaseModel() {

    val result = MutableStateFlow(BigInteger.ZERO)
    val executionTime = MutableStateFlow<Int?>(null)
    val executing = MutableStateFlow(false)
    val errorMessage = MutableStateFlow<String?>(null)
    val showLargeIterationWarning = MutableStateFlow(false)

    private fun doWork(platform: String, type: String, iteration: Int, repeat: Int) {
        val benchResult = BenchRunnerUtil.run(platform, type, iteration, repeat, "")
        result.value = benchResult.second
        executionTime.value = benchResult.first
        errorMessage.value = benchResult.third
    }

    fun onExecute(config: BenchConfig) {
        val platform = config.platform.value
        val type = config.type.value
        val iteration = config.iteration.value.toIntOrNull() ?: 1000000
        val repeat = config.repeat.value.toIntOrNull() ?: 5

        if (!config.acceptLargeIteration.value && iteration > 100000) {
            showLargeIterationWarning.value = true
            return
        }

        result.value = BigInteger.ZERO
        executionTime.value = 0
        executing.value = true

        viewModelScope.launch(Dispatchers.IO) {
            doWork(platform, type, iteration, repeat)
            executing.value = false
        }
    }

    fun dismissLargeIterationWarning() {
        showLargeIterationWarning.value = false
    }

}