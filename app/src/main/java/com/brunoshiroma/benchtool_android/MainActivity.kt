package com.brunoshiroma.benchtool_android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.brunoshiroma.benchtool_android.databinding.ActivityMainBinding
import com.brunoshiroma.benchtool_android.model.BenchConfig
import com.brunoshiroma.benchtool_android.model.BenchResult
import com.brunoshiroma.benchtool_android.model.Device
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.play.core.splitcompat.SplitCompat
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import dalvik.system.BaseDexClassLoader
import java.math.BigInteger


class MainActivity : AppCompatActivity() {

    interface CLibrary : Library {
        fun benchtoolGoCall(iteration: Int, repeat: Int, bench_type: Int) : String

        companion object {
            val INSTANCE = Native.load("benchtool-go-lib-android-arm64",
                CLibrary::class.java
            ) as CLibrary
        }
    }

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
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        val bottom: BottomAppBar = this.findViewById<BottomAppBar>(R.id.bottomAppBar)

        benchResult.config.set(config)
        benchResult.executing.set(false)

        config.iteration.set("100000")
        config.repeat.set("10")
        config.platform.set("go")

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

        bottom.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.page_5 -> {
                    val aboutActivity = Intent(this, AboutActivity::class.java)
                    startActivity(aboutActivity)
                    true
                }
                else -> false
            }
        }

        val success = SplitCompat.installActivity(this)

    }

}