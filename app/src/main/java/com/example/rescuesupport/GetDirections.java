package com.example.rescuesupport;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import java.util.List;

public class GetDirections extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private GeoApiContext mGeoApiContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map_support);

        // Google 지도 API 사용을 위한 설정
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        // GeoApiContext 초기화
        String apiKey = getString(R.string.google_maps_key);
        mGeoApiContext = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 위치 권한 체크
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        mMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

            // 사용자의 현재 위치에 마커 추가
            mMap.addMarker(new MarkerOptions().position(userLocation).title("현재 위치"));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

            // 주소를 좌표로 변환
            String fullAddress = getIntent().getStringExtra("selectedItem");
            if (fullAddress != null && !fullAddress.isEmpty()) {
                String[] splitAddress = fullAddress.split("\n");
                if (splitAddress.length > 1) {
                    String detailedAddress = splitAddress[1];  // 상세주소 부분만 추출
                    LatLng destinationLatLng = getLatLngFromAddress(detailedAddress);
                    if (destinationLatLng != null) {
                        // 목적지에 마커 추가
                        mMap.addMarker(new MarkerOptions().position(destinationLatLng).title(detailedAddress));

                        // 경로 그리기
                        getDirections(userLocation, destinationLatLng);
                    }
                }
            }
        }
    }
    // 길찾기 요청 메소드
    private void getDirections(LatLng origin, LatLng destination) {
        com.google.maps.model.LatLng originLatLng = new com.google.maps.model.LatLng(origin.latitude, origin.longitude);
        com.google.maps.model.LatLng destinationLatLng = new com.google.maps.model.LatLng(destination.latitude, destination.longitude);

        // 로그에 출발지와 목적지의 위도 및 경도 값 출력
        Log.d("DirectionsAPI", "출발지: " + originLatLng.toString());
        Log.d("DirectionsAPI", "목적지: " + destinationLatLng.toString());

        try {
            DirectionsResult result = DirectionsApi.newRequest(mGeoApiContext)
                    .mode(TravelMode.TRANSIT)
                    .origin(originLatLng)
                    .destination(destinationLatLng)
                    .await();

            // API 응답 전체 출력
            Log.d("DirectionsAPI", "API 응답: " + result.toString());

            if (result != null && result.routes != null && result.routes.length > 0) {
                DirectionsRoute route = result.routes[0];
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.BLUE);
                polylineOptions.width(10);
                for (com.google.maps.model.LatLng point : route.overviewPolyline.decodePath()) {
                    polylineOptions.add(new LatLng(point.lat, point.lng));
                }
                mMap.addPolyline(polylineOptions);
            }
        } catch (Exception e) {
            Log.e("DirectionsAPI", "API 오류", e);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우 지도를 다시 로드
                onMapReady(mMap);
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private LatLng getLatLngFromAddress(String address) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        LatLng latLng = null;

        try {
            addresses = geocoder.getFromLocationName(address, 1); // 1은 반환할 최대 결과 수를 의미합니다.
            if (addresses != null && !addresses.isEmpty()) {
                Address returnedAddress = addresses.get(0);
                latLng = new LatLng(returnedAddress.getLatitude(), returnedAddress.getLongitude());
            } else {
                Log.w("Address", "No Address returned!");
            }
        } catch (Exception e) {
            Log.e("Address", "Geocoder failed", e);
        }

        return latLng;
    }

}
