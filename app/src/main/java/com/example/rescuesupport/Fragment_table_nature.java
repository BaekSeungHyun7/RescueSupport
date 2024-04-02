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

public class Fragment_table_nature extends Fragment {

    private WebView webView;
    private Map<Integer, String> buttonUrlMap;

    public Fragment_table_nature() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.table_disaster_nature, container, false);
        webView = rootView.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        setupButtonUrlMap();
        setupButtonListeners(rootView);

        return rootView;
    }

    private void setupButtonUrlMap() {
        buttonUrlMap = new HashMap<>();
        buttonUrlMap.put(R.id.btn_drought, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/drought.html");
        buttonUrlMap.put(R.id.btn_gale, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/gale.html");
        buttonUrlMap.put(R.id.btn_thunder_stroke, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/thunderstroke.html");
        buttonUrlMap.put(R.id.btn_green_algae, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/algal.html");
        buttonUrlMap.put(R.id.btn_heavy_snow, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/heavySnow.html");
        buttonUrlMap.put(R.id.btn_land_slide, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/landSlide.html");
        buttonUrlMap.put(R.id.btn_yellow_dust, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/dustStorm.html");
        buttonUrlMap.put(R.id.btn_particulate, "http://m.airkorea.or.kr/main");
        buttonUrlMap.put(R.id.btn_red_tide, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/redTide.html");
        buttonUrlMap.put(R.id.btn_earthquake, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/earthquake.html");
        buttonUrlMap.put(R.id.btn_Flooding, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/flood.html");
        buttonUrlMap.put(R.id.btn_Typhoon, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/typhoon.html");
        buttonUrlMap.put(R.id.btn_heat_wave, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/heatWave.html");
        buttonUrlMap.put(R.id.btn_cold_wave, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/coldWave.html");
        buttonUrlMap.put(R.id.btn_Tsunami, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/tsunami2.html");
        buttonUrlMap.put(R.id.btn_volcano, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/volcano.html");
        buttonUrlMap.put(R.id.btn_jellyfish, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/jellyfish.html");
        buttonUrlMap.put(R.id.btn_downpour, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/nat/downpour.html");
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




