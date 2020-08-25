package com.brunoshiroma.benchtool_android

import android.app.Application
import android.content.pm.ApplicationInfo
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory

class BenchtoolApplication : Application(), Lazy<BenchtoolApplication> {

    companion object{
        lateinit var appInfo: ApplicationInfo
        lateinit var app : Lazy<BenchtoolApplication>
        lateinit var manager: SplitInstallManager
    }

    override fun onCreate() {
        super.onCreate()
        appInfo = this.applicationInfo
        app = this
        manager = SplitInstallManagerFactory.create(this)
    }

    override val value: BenchtoolApplication
        get() = this

    override fun isInitialized(): Boolean {
        return true
    }

}