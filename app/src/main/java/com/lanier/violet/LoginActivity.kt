package com.lanier.violet

import android.content.DialogInterface
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.toColorInt
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import com.lanier.violet.client.RocoServerClient
import com.lanier.violet.databinding.ActivityLoginBinding
import org.apache.commons.text.StringEscapeUtils
import kotlin.random.Random

class LoginActivity(
    override val layoutId: Int = R.layout.activity_login
) : BaseAct<ActivityLoginBinding>() {

    private var channel: String = ""

    override fun onBind() {
        viewbinding.cbRandomChannel.setOnCheckedChangeListener { _, b ->
            viewbinding.tilChannel.visibleOrGone(!b)
            if (b) {
                channel = Random.nextInt(1, 300).toString()
                viewbinding.tvRandomChannel.text = channel
                viewbinding.tvRandomChannel.setTextColor(Color.BLACK)
            } else {
                viewbinding.tvRandomChannel.text = buildSpannedString {
                    if (channel.isNotEmpty()) {
                        inSpans(
                            ForegroundColorSpan("#B5B5B5".toColorInt()),
                            StrikethroughSpan(),
                        ) {
                            append(channel)
                        }
                    }
                }
            }
        }

        viewbinding.webview.apply {
            settings.run {
                javaScriptEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
            }
            webViewClient = object : WebViewClient() {

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

                                    if (viewbinding.cbRandomChannel.isChecked.not()) {
                                        val inputChannel = viewbinding.tieChannel.text?.toString()
                                        inputChannel?.let {
                                            RocoServerClient.channel = it
                                            startAct<MainActivity> {  }
                                        } ?: toast("输入频道~")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                }
            }

            val loginUrl =
                "https://graph.qq.com/oauth2.0/show?which=Login&display=pc&response_type=code&client_id=102061779&redirect_uri=https%3A%2F%2F17roco.qq.com%2Flogintarget.html&scope=all"
            loadUrl(loginUrl)
        }
    }

    private fun loadData(url: String) {
        if (url.contains("web2.17roco.qq.com/fcgi-bin/login")) {
            viewbinding.webview.loadUrl("about:blank")
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