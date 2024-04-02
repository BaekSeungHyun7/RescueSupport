package com.example.rescuesupport;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainTablebtnCPRActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_table_cpr);

        // WebView 설정
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // JavaScript 활성화

        // WebViewClient 설정 - 이 설정을 하면 웹 페이지를 WebView 내부에서 로드
        webView.setWebViewClient(new WebViewClient());

        // 웹 페이지를 로드
        webView.loadUrl("https://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/CPR.html");
    }
}
