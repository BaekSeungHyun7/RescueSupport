package com.example.rescuesupport;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AEDMapActivity extends FragmentActivity {

    private GoogleMap googleMap;
    private Location currentLocation;
    private ArrayList<String> aedLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aed);

        // 인텐트에서 전달된 현재 위치와 AED 위치 정보를 가져옴
        currentLocation = getIntent().getParcelableExtra("current_location");
        aedLocations = getIntent().getStringArrayListExtra("aed_locations");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::setUpMap);
    }

    private void setUpMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (currentLocation != null) {
            // 현재 위치로 카메라 이동 및 줌 설정
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));

            // 현재 위치에 마커 추가
            googleMap.addMarker(new MarkerOptions().position(currentLatLng).title("현재 위치"));

            // 현재 위치에 반경 1km의 투명한 파란색 원을 그림
            googleMap.addCircle(new CircleOptions()
                    .center(currentLatLng) // 원의 중심
                    .radius(1000) // 반경을 미터 단위로 설정
                    .strokeWidth(0f) // 선의 너비. 여기서는 선이 없음을 의미함
                    .fillColor(0x220000FF)); // 투명한 파란색을 ARGB 형식으로 설정
        }

        // AED 위치에 파란색 마커 추가
        for (String location : aedLocations) {
            String[] parts = location.split("\n");
            String name = parts[0];
            String address = parts[1];

            // 주소를 좌표로 변환하고 마커를 찍는 작업을 비동기적으로 실행
            new Thread(() -> {
                LatLng latLng = getLatLngFromAddress(address);
                if (latLng != null) {
                    // UI 스레드에서 파란색 마커 추가
                    runOnUiThread(() -> googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(name)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
                }
            }).start();
        }
    }


    private LatLng getLatLngFromAddress(String address) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        try {
            // 주소로부터 위치 정보를 가져옴
            addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

