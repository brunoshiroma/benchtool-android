package com.brunoshiroma.benchtool_android


import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.brunoshiroma.benchtool_android.runner.BenchRunnerUtil
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.io.File

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

        var packageName : String = "com.brunoshiroma.benchtool_android"

        if(BuildConfig.DEBUG) {
          packageName += ".debug"
        }

        assertEquals(packageName, appContext.packageName)
    }

    /*
    Checks native libs on right dir
     */
    @Test
    fun bTestNativeLibFiles(){
        val libsCount = File(appContext.applicationInfo.nativeLibraryDir).listFiles()!!.size
        assertThat(libsCount, greaterThan(0))
    }

    @Test
    fun cTestExecuteBench(){

        val activityScenario: ActivityScenario<MainActivity> = ActivityScenario.launch(
            MainActivity::class.java
        )

        activityScenario.onActivity {
            IdlingRegistry.getInstance().register(*BenchRunnerUtil.getIdle())
        }

        //check text before bench execute is 0ms
        onView(withId(R.id.textView3))
            .check(matches(withText("0ms")))

        //execute the bench, with default values
        onView(withId(R.id.bench_button))
            .perform(click())

        //check if the bench executes and the result in ms is on textview
        onView(withId(R.id.textView3))
            .check(matches(not(withText("0ms"))))

    }

}