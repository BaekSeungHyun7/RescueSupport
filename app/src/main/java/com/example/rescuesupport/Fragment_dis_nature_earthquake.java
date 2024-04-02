package com.example.rescuesupport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.Fragment;

public class Fragment_dis_nature_earthquake extends Fragment {

    public Fragment_dis_nature_earthquake() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dis_webview, container, false);

        // WebView를 XML 레이아웃과 연결
        WebView webView = rootView.findViewById(R.id.webViewEarthquake);

        // 웹뷰 설정
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 자바스크립트 사용 가능하도록 설정

        // WebViewClient를 설정하여 웹페이지가 앱 내에서 열리도록 함
        webView.setWebViewClient(new WebViewClient());

        // URL 로드
        String url = "https://www.safekorea.go.kr/idsiSFK/neo/sfk/cs/contents/prevent/prevent09.html?menuSeq=126";
        webView.loadUrl(url);

        return rootView;
    }
}

