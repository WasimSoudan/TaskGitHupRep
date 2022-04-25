package com.example.githubapp.Data

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.githubapp.R
/*This activity implements to show the user webpage for the Repositories when clicking on image view
 in the main activity when taking html_url from the API and showing*/
class webapp : AppCompatActivity() {
    lateinit var WebView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webapp)
        val url :String =intent.getStringExtra("url").toString()
        Log.d("url",url)
        WebView=findViewById(R.id.WebView)
        WebView.settings.setJavaScriptEnabled(true)
        WebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url.toString())
                return true
            }
        }
        WebView.loadUrl(url)
    }

    override fun onBackPressed() {
        if(WebView.canGoBack()){
            WebView.goBack()
        }else{
            super.onBackPressed()
        }
    }
}