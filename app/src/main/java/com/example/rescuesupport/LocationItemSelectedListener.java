package com.example.rescuesupport;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class LocationItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private final Fragment_search_shelter activity;
    private final Context context;

    public LocationItemSelectedListener(Fragment_search_shelter activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        String[] subLocationArray = null;

        switch (selectedItem) {
            case "서울특별시": subLocationArray = context.getResources().getStringArray(R.array.Seoul);
                break;
            case "부산광역시": subLocationArray = context.getResources().getStringArray(R.array.Busan);
                break;
            case "대구광역시": subLocationArray = context.getResources().getStringArray(R.array.Daegu);
                break;
            case "인천광역시": subLocationArray = context.getResources().getStringArray(R.array.Incheon);
                break;
            case "광주광역시": subLocationArray = context.getResources().getStringArray(R.array.Gwangju);
                break;
            case "대전광역시": subLocationArray = context.getResources().getStringArray(R.array.Daejeon);
                break;
            case "울산광역시": subLocationArray = context.getResources().getStringArray(R.array.Ulsan);
                break;
            case "세종특별자치시": subLocationArray = context.getResources().getStringArray(R.array.Sejong);
                break;
            case "경기도": subLocationArray = context.getResources().getStringArray(R.array.Gyeonggido);
                break;
            case "강원특별자치도": subLocationArray = context.getResources().getStringArray(R.array.Gangwondo);
                break;
            case "충청북도": subLocationArray = context.getResources().getStringArray(R.array.Chungcheongbukdo);
                break;
            case "충청남도": subLocationArray = context.getResources().getStringArray(R.array.Chungcheongnamdo);
                break;
            case "전라북도": subLocationArray = context.getResources().getStringArray(R.array.Jeollabukdo);
                break;
            case "전라남도": subLocationArray = context.getResources().getStringArray(R.array.Jeollanamdo);
                break;
            case "경상북도": subLocationArray = context.getResources().getStringArray(R.array.Gyeongsangbukdo);
                break;
            case "경상남도": subLocationArray = context.getResources().getStringArray(R.array.Gyeongsangnamdo);
                break;
            case "제주특별자치도": subLocationArray = context.getResources().getStringArray(R.array.Jejudo);
                break;
        }

        if (subLocationArray != null) {
            Spinner spinnerSubLocation = activity.getSpinnerSubLocation();
            ArrayAdapter<String> subLocationAdapter = new ArrayAdapter<>(
                    context,
                    android.R.layout.simple_spinner_item,
                    subLocationArray
            );
            subLocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSubLocation.setAdapter(subLocationAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}
