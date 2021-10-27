package org.weyoung.stockcaculator.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import com.google.gson.Gson
import org.weyoung.stockcaculator.data.model.StockData
import org.weyoung.stockcaculator.data.remote.webPageUrl


@SuppressLint("SetJavaScriptEnabled", "ModifierParameter", "JavascriptInterface")
@Composable
fun HiddenWebpage(url: String, token: (String) -> Unit, result: (StockData) -> Unit) {
    AndroidView(
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(1, 1)
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.userAgentString =
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36"
                addJavascriptInterface(object : StockInterface {
                    @JavascriptInterface
                    override fun result(content: String) {
                        content.takeIf { it != "undefined" }?.let {
                            val stockData = Gson().fromJson(content, StockData::class.java)
                            token(stockData.token)
                            result(stockData)
                        }
                    }
                }, "hack")
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ) = false

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        loadUrl("javascript:hack.result(JSON.stringify(allResult))");
                    }
                }
                loadUrl(url)
            }
        },
        update = {
            it.loadUrl(url)
        })
}

@Composable
fun webViewState() = remember {
    WebViewState(webPageUrl, StockData("", emptyList()))
}

data class WebViewState(var url: String, var data: StockData)

interface StockInterface {
    fun result(content: String)
}