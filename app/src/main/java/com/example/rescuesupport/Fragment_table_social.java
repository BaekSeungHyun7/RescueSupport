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

public class Fragment_table_social extends Fragment {
    private WebView webView;
    private Map<Integer, String> buttonUrlMap;

    public Fragment_table_social() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.table_disaster_social, container, false);
        webView = rootView.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        setupButtonUrlMap();
        setupButtonListeners(rootView);

        return rootView;
    }

    private void setupButtonUrlMap() {
        buttonUrlMap = new HashMap<>();
        buttonUrlMap.put(R.id.btn_gas_exposure, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/explosion.html");
        buttonUrlMap.put(R.id.btn_livestock, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/livestockdisease.html");
        buttonUrlMap.put(R.id.btn_Infectious, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/infectiousdisease.html");
        buttonUrlMap.put(R.id.btn_building_collapse, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/building.html");
        buttonUrlMap.put(R.id.btn_Traffic_Accident, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/traffic.html");
        buttonUrlMap.put(R.id.btn_military_damage, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/set/civilAirDefence.html");
        buttonUrlMap.put(R.id.btn_Dam_collapse, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/dam.html");
        buttonUrlMap.put(R.id.btn_tunnel_accident, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/tunnel.html");
        buttonUrlMap.put(R.id.btn_financialcom, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/financialcom.html");
        buttonUrlMap.put(R.id.btn_health_disaster, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/healthCare.html");
        buttonUrlMap.put(R.id.btn_workplace_accident, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/workplace.html");
        buttonUrlMap.put(R.id.btn_nuclear_accident, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/nuclearaccident.html");
        buttonUrlMap.put(R.id.btn_overcrowding, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/concertHall.html");
        buttonUrlMap.put(R.id.btn_electric_accident, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/electricGas.html");
        buttonUrlMap.put(R.id.btn_Outages, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/blackout.html");
        buttonUrlMap.put(R.id.btn_subway_accident, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/rail.html");
        buttonUrlMap.put(R.id.btn_terror, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/terror.html");
        buttonUrlMap.put(R.id.btn_information, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/lit/information.html");
        buttonUrlMap.put(R.id.btn_aircraft_accident, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/aircraft.html");
        buttonUrlMap.put(R.id.btn_marine_ship_accident, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/shipAccident.html");
        buttonUrlMap.put(R.id.btn_marine_pollution, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/mPollutionAcc.html");
        buttonUrlMap.put(R.id.btn_nucleus, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/set/CBP.html");
        buttonUrlMap.put(R.id.btn_fire, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/fire.html");
        buttonUrlMap.put(R.id.btn_chemical_accident, "http://www.safekorea.go.kr/idsiSFK/neo/main_m/sot/chemical.html");
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