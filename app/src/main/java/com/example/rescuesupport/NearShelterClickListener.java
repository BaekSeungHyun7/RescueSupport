package com.example.rescuesupport;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class NearShelterClickListener implements View.OnClickListener {
    private final Context context;

    public NearShelterClickListener(Context context) {
        this.context = context;
    }
    private final HashMap<String, LatLng> shelterLocations = new HashMap<>();{
        shelterLocations.put("민방위대피소", new LatLng(37.5665, 126.9780));
        // 여기에 다른 대피소 위치를 추가할 수 있습니다.
    }
    @Override
    public void onClick(View v) {
        final CharSequence[] items = {
                "민방위대피소",
                "지진옥외대피장소",
                "지진해일긴급대피장소",
                "응급의료센터",
                "병원",
                "약국",
                "소방서",
                "경찰서"
        };

        // 대피소 선택 다이얼로그를 보여줍니다.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("대피소 종류 선택");
        builder.setItems(items, (dialog, which) -> {
            String selectedShelterType = items[which].toString();

            // NearShelter 액티비티를 시작하고 선택된 대피소 종류를 전달합니다.
            Intent intent = new Intent(context, NearShelter.class);
            intent.putExtra("SELECTED_SHELTER_TYPE", selectedShelterType);
            LatLng location = shelterLocations.get(selectedShelterType);
            if (location != null) {
                intent.putExtra("LOCATION_LAT", location.latitude);
                intent.putExtra("LOCATION_LNG", location.longitude);
            }
            context.startActivity(intent);
        });
        builder.show();
    }
}


