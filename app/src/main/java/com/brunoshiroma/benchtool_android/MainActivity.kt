package com.brunoshiroma.benchtool_android

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.brunoshiroma.benchtool_android.databinding.ActivityMainBinding
import com.brunoshiroma.benchtool_android.model.BenchConfig
import com.brunoshiroma.benchtool_android.model.BenchResult
import com.brunoshiroma.benchtool_android.model.Device
import java.math.BigInteger

class MainActivity : AppCompatActivity() {

    private val benchResult : BenchResult by lazy{
        ViewModelProvider(this).get(BenchResult::class.java)
    }

    private val device : Device by lazy{
        ViewModelProvider(this).get(Device::class.java)
    }

    private val config : BenchConfig by lazy{
        ViewModelProvider(this).get(BenchConfig::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        BenchRunner.setup(this.applicationInfo.nativeLibraryDir)

        benchResult.config.set(config)
        benchResult.executing.set(false)

        config.iteration.set("100000")
        config.repeat.set("10")

        binding.bench = benchResult

        binding.config = config

        binding.device = device

        benchResult.result.set(BigInteger.TEN)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            device.osArch.set(Build.SUPPORTED_ABIS.joinToString(", "))
        } else {
            device.osArch.set(Build.CPU_ABI)
        }
        device.libDir.set(this.applicationInfo.nativeLibraryDir)

    }
}