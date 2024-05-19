package com.lanier.violet

import android.content.DialogInterface
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.apache.commons.text.StringEscapeUtils

class MainActivity : AppCompatActivity() {

    private val webView by lazy { findViewById<WebView>(R.id.webView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        webView.settings.run {
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                return super.shouldInterceptRequest(view, request)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }

            override fun onPageFinished(webview: WebView, url: String?) {
                super.onPageFinished(webview, url)
                println(">>>> on page finished $url")
                val code = "(function() { return document.documentElement.outerHTML; })();".trimIndent()
                val mUrl = url.toString()
                loadData(mUrl)
                webview.evaluateJavascript(code) { value ->
                    val htmlContent = if (value == null) {
                        ""
                    } else {
                        StringEscapeUtils.unescapeEcmaScript(value)
                    }
                    if (url!!.contains("web2.17roco.qq.com/fcgi-bin/login")) {
                        if (htmlContent.isNotEmpty()) {
                            val isNight = htmlContent.contains("res.17roco.qq.com/swf/night.swf")
                            if (isNight) {
                                serverRepair()
                            } else {
                                UserData.mainKey =
                                    extractString(htmlContent, "&angel_key=", "&skey=")
                                UserData.farmKey = extractString(htmlContent, "pskey=", "\"")
                                UserData.QQ = extractString(htmlContent, "_uin=", "&ang")
                            }
                        }
                    }
                }
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }
        }
        webView.clearCache(true)

        val loginUrl =
            "https://graph.qq.com/oauth2.0/show?which=Login&display=pc&response_type=code&client_id=102061779&redirect_uri=https%3A%2F%2F17roco.qq.com%2Flogintarget.html&scope=all"
        webView.loadUrl(loginUrl)
    }

    private fun loadData(url: String) {
        if (url.contains("web2.17roco.qq.com/fcgi-bin/login")) {
            webView.loadUrl("about:blank")
            CookieManager.getInstance().run {
                setAcceptCookie(true)
                val mCookie = getCookie(url)
                setCookie("Accept-Encoding", "gzip, deflate")
                setCookie("Connection", "keep-alive")
                setCookie("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.6261.95 Safari/537.36")
                setCookie("Accept-Language", "zh-CN,zh-CN;q=0.8,en-US;q=0.6")
                setCookie("Version", "1.0")
                setCookie("Cookie", mCookie)
                flush()
                println(">>>> 设置cookie $mCookie")
            }
        }
    }

    private fun serverRepair() {
        val onClickListener = DialogInterface.OnClickListener { p0, p1 ->
            if (p1 == DialogInterface.BUTTON_POSITIVE) {
                p0?.dismiss()
            }
        }
        AlertDialog.Builder(this)
            .setMessage("将于明日6点开放")
            .setPositiveButton("确定", onClickListener)
            .setCancelable(false)
            .show()
    }

    private fun extractString(source: String, startDelimiter: String, endDelimiter: String): String {
        val regex = Regex("""$startDelimiter(.*?)$endDelimiter""")
        val matchResult = regex.find(source)
        return matchResult?.groupValues?.get(1)?:""
    }
}