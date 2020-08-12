package com.brunoshiroma.benchtool_android

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.File

import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class BenchtoolInstrumentedTest {

    private lateinit var appContext : Context

    @Before
    fun setup(){
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun aTestUseAppContext() {
        assertEquals("com.brunoshiroma.benchtool_android", appContext.packageName)
    }

    @Test
    fun bTestNativeLibFiles(){
        val libsCount = File(appContext.applicationInfo.nativeLibraryDir).listFiles()!!.size
        assertThat(libsCount, greaterThan(0))
    }

}