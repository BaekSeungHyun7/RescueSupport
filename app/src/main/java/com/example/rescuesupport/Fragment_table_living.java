package com.example.rescuesupport;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class Fragment_table_living extends Fragment {
    private WebView webView;
    private Map<Integer, String> buttonUrlMap;

    public Fragment_table_living() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.table_disaster_living, container, false);
        webView = rootView.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        setupButtonUrlMap();
        setupButtonListeners(rootView);

        return rootView;
    }

    private void setupButtonUrlMap() {
        buttonUrlMap = new HashMap<>();
        buttonUrlMap.put(R.id.btn_water_accident, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/summer.html");
        buttonUrlMap.put(R.id.btn_mountain_accident, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/hiking.html");
        buttonUrlMap.put(R.id.btn_elevator_accident, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/elevator.html");
        buttonUrlMap.put(R.id.btn_food_poisoning, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/foodPoison.html");
        buttonUrlMap.put(R.id.btn_CPR, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/CPR.html");
        buttonUrlMap.put(R.id.btn_first_aid, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/emergency.html");
        buttonUrlMap.put(R.id.btn_bug, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/ant.html");
        buttonUrlMap.put(R.id.btn_kidnap, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/kidnap.html");
        buttonUrlMap.put(R.id.btn_oilProductAccident, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/oilProductAccident.html");
    }
    private void setupButtonListeners(View rootView) {
        for (Integer buttonId : buttonUrlMap.keySet()) {
            Button button = rootView.findViewById(buttonId);
            final String url = buttonUrlMap.get(buttonId);
            button.setOnClickListener(v -> openWebPage(url));
        }
    }

    private void openWebPage(String url) {
        webView.loadUrl(url);
        webView.setVisibility(View.VISIBLE);
    }

}