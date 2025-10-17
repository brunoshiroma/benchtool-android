package com.brunoshiroma.benchtool_android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.ViewCompat
import com.brunoshiroma.benchtool_android.components.AboutContent
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class AboutActivity : AppCompatActivity() {

    lateinit var mAdView : AdView

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets -> insets }

        MobileAds.initialize(this) {}

        setContentView(
            ComposeView(this).apply {
                setContent {
                    AboutContent(activity = this@AboutActivity)
                }
            }
        )



    }
}