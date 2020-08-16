package com.brunoshiroma.benchtool_android

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import com.google.android.material.appbar.MaterialToolbar

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val appbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(appbar)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val aboutText = findViewById<TextView>(R.id.about_text)

        var html = getString(R.string.about_html)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            aboutText.text = Html.fromHtml(html, FROM_HTML_MODE_COMPACT)
        } else {
            aboutText.text = Html.fromHtml(html)
        }
        aboutText.movementMethod =LinkMovementMethod.getInstance()
    }
}