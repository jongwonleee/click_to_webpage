package com.jongwonleee.fastwebpageviewer

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.cielyang.android.clearableedittext.ClearableEditText

class MainActivity : AppCompatActivity() {
    private val inputUrl by lazy { findViewById<ClearableEditText>(R.id.edit_uri) }
    private val webView by lazy { findViewById<WebView>(R.id.web_view) }
    private val sharedPreference by lazy{
    getSharedPreferences("url",Activity.MODE_PRIVATE)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView.settings.javaScriptEnabled=true
        webView.webViewClient = object: WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.let{
                    request?.let { it.url.toString() }?.let { it1 -> it.loadUrl(it1) }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        var url:String? = if (intent.getStringExtra(Intent.EXTRA_TEXT) != null)
            intent.getStringExtra(Intent.EXTRA_TEXT)
        else sharedPreference.getString("url",null)

        url?.let{
            openUrl(it)
            inputUrl.setText(it)
        }
    }
    fun onSaveButtonClick(v: View){
        val input = inputUrl.text.toString()
        openUrl(input)
    }
    private fun openUrl(url:String){
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if(URLUtil.isValidUrl(url)){
            webView.loadUrl(url)
            imm.hideSoftInputFromWindow(inputUrl.windowToken,0)
            val editor = sharedPreference.edit()
            editor.putString("url",url)
            editor.commit()
        }else
        {
            Toast.makeText(this, resources.getString(R.string.error), Toast.LENGTH_LONG).show()
            inputUrl.setText("")
        }
    }
}