package com.brunoshiroma.benchtool_android.components

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.brunoshiroma.benchtool_android.BenchtoolApplication
import com.brunoshiroma.benchtool_android.BuildConfig
import com.brunoshiroma.benchtool_android.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AboutContent(modifier: Modifier = Modifier,
                 scrollBehavior: TopAppBarScrollBehavior? = null,
                 actions: @Composable RowScope.() -> Unit = {},
                 activity: AppCompatActivity? = null) {
    val themePrimaryColor = colorResource(R.color.colorPrimary)
    val adUnitId = stringResource(R.string.bench_ads)
    val adView = remember { AdView(BenchtoolApplication.app.value) }
    adView.adUnitId = adUnitId
    val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(LocalContext.current, 360)
    adView.setAdSize(adSize)

    val buildDate = BuildConfig.BUILD_DATE
    val buildVersion = BuildConfig.VERSION_NAME
    val aboutHtml = stringResource(R.string.about_html)
        .replace("\$VERSION", buildVersion)
        .replace("\$DATE", buildDate)

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = modifier,
                actions = actions,
                scrollBehavior = scrollBehavior,
                title = { Text("About") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = themePrimaryColor
                ),
                navigationIcon =
                    {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    activity?.finish()
                                }
                        )
                    }

            )
        },
        content = { paddingValues ->
            Column (modifier = Modifier.padding(paddingValues), verticalArrangement = Arrangement.Bottom) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5F),
                ){
                    Text(
                        AnnotatedString.fromHtml(
                            htmlString = aboutHtml,
                            linkStyles = TextLinkStyles(style = SpanStyle(
                                textDecoration = TextDecoration.Underline,
                                fontStyle = FontStyle.Italic
                            ))))
                }
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                ) {
                    Box(modifier = modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(color = colorResource(R.color.colorPrimary) )) {
                        AndroidView(
                            modifier = modifier.clip(RoundedCornerShape(10)),
                            factory = { context ->
                                adView
                            }
                        )
                    }
                }
            }
        },
    )

    LifecycleResumeEffect(adView) {
        adView.resume()
        onPauseOrDispose { adView.pause() }
    }

    adView.loadAd(AdRequest.Builder().build())
}