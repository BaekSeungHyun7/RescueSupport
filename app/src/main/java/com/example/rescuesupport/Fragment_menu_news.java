package com.example.rescuesupport;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

public class Fragment_menu_news extends Fragment {

    private WebView webView;

    public Fragment_menu_news() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_news, container, false);

        // WebView 초기화 및 설정
        webView = view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        // 웹 페이지 로딩 시작 및 완료 시의 이벤트 처리
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // 웹 페이지 로딩 화면 다이얼 로그
                BottomNavigationHelper.showLoadingDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 로딩 다이얼 로그를 숨김
                BottomNavigationHelper.hideLoadingDialog();
            }
        });

        // 웹 페이지를 로드하고 보여주기
        webView.loadUrl("https://www.hjnews.co.kr/");

        return view;
    }

}
