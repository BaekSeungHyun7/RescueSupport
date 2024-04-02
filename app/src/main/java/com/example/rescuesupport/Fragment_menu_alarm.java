package com.example.rescuesupport;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class Fragment_menu_alarm extends Fragment {

    private CurrentLocationHelper locationHelper;
    private WebView webView;

    public Fragment_menu_alarm() {
        // 필수 빈 생성자 (Required empty public constructor)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu_alarm, container, false);

        // WebView 초기화
        webView = rootView.findViewById(R.id.disaster_text);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setEnableSmoothTransition(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // 웹 페이지 로딩 시작 시 로딩 다이얼로그 표시
                BottomNavigationHelper.showLoadingDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 웹 페이지 로딩 완료 시 로딩 다이얼로그 숨기기
                BottomNavigationHelper.hideLoadingDialog();
            }
        });

        // URL 로드
        webView.loadUrl("https://m.safekorea.go.kr/idsiSFK/neo/main_m/dis/disasterDataList.html");

        return rootView;
    }

}
























