package com.example.rescuesupport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.Toast;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AEDClickListener implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Context context; // 액티비티의 컨텍스트 참조
    private LocationManager locationManager;
    private ArrayList<String> aedLocations;

    // 생성자에서 컨텍스트와 위치 관리자를 전달받음
    public AEDClickListener(Context context, LocationManager locationManager) {
        this.context = context;
        this.locationManager = locationManager;
        aedLocations = new ArrayList<>();
        parseAEDLocations();
    }

    //AED 위치 정보를 파싱하는 메소드
    private void parseAEDLocations() {
        Resources res = context.getResources();
        String[] locations = res.getStringArray(R.array.AED_경기도_용인시_기흥구_구갈동);
        aedLocations.addAll(Arrays.asList(locations));
    }


    @Override
    public void onClick(View view) {
        // 위치 권한이 있는지 확인
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없다면 사용자에게 요청
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            // 권한이 이미 있다면 위치를 가져옴
            getLocationAndStartMapActivity();
        }
    }

    private void getLocationAndStartMapActivity() {
        try {
            Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (currentLocation == null) {
                Toast.makeText(context, "Cannot get current location", Toast.LENGTH_SHORT).show();
                return;
            }

            // 구글 맵을 띄우는 액티비티를 시작하는 인텐트 생성
            Intent intent = new Intent(context, AEDMapActivity.class);
            intent.putExtra("current_location", currentLocation);
            intent.putStringArrayListExtra("aed_locations", aedLocations);
            context.startActivity(intent);
        } catch (SecurityException e) {
            Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }
}


