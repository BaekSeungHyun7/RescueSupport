package com.example.rescuesupport;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NearShelter extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient mLocationProviderClient;
    private String selectedShelterType;
    private GeoApiContext mGeoApiContext;
    private View loadingView;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearshelter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // 로딩 화면 초기화
        loadingView = findViewById(R.id.loading_view);
        loadingView.setVisibility(View.VISIBLE);

        // GeoApiContext 초기화
        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_key))
                    .build();
        }

        // 위치 서비스 클라이언트 초기화
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // 권한이 있는 경우, 지도를 가져옵니다.
            setupMap();
        } else {
            // 권한이 없는 경우, 권한을 요청합니다.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // NearShelterClickListener에서 전달받은 대피소 종류
        selectedShelterType = getIntent().getStringExtra("SELECTED_SHELTER_TYPE");

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여되었으면 위치 업데이트를 다시 시도합니다.
                getDeviceLocation();
            }
        }
    }
    private void setupMap() {
        ((SupportMapFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.map))).getMapAsync(this);
    }
    private void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        mLocationProviderClient.getLastLocation().addOnCompleteListener(this, task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                // 현재 위치를 찾았으면 지도의 중심을 사용자의 위치로 설정
                Location lastKnownLocation = task.getResult();
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

                // 현재 위치에 마커를 추가
                mMap.addMarker(new MarkerOptions().position(userLocation).title("현재 위치"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                // Geocoder
                Geocoder geocoder = new Geocoder(NearShelter.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(userLocation.latitude, userLocation.longitude, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        // 주소를 가져왔으면, 가장 가까운 대피소를 찾습니다.
                        String currentAddress = addresses.get(0).getAddressLine(0);
                        findNearestShelter(currentAddress, userLocation);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "서비스 사용 불가", e);
                }
            } else {
                // 위치를 찾지 못했을 때의 처리 로직
                Log.e(TAG, "Exception: %s", task.getException());
            }
        });
    }
    private void findNearestShelter(String currentAddress, LatLng userLocation) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String[] addressParts = currentAddress.split(" ");
        String shelterArrayName = "shelter_" + addressParts[1] + "_" + addressParts[2] + "_" + addressParts[3] + "_" + selectedShelterType;
        @SuppressLint("DiscouragedApi") int shelterArrayResId = getResources().getIdentifier(shelterArrayName, "array", getPackageName());

        if (shelterArrayResId != 0) {
            String[] shelters = getResources().getStringArray(shelterArrayResId);
            LatLng nearestShelterLocation = null;
            float nearestDistance = Float.MAX_VALUE;
            String nearestShelterInfo = "";

            for (String shelter : shelters) {
                String[] shelterInfo = shelter.split("\n");
                String shelterName = shelterInfo[0];
                String shelterAddress = shelterInfo[1];

                try {
                    List<Address> shelterAddresses = geocoder.getFromLocationName(shelterAddress, 1);
                    if (shelterAddresses != null && !shelterAddresses.isEmpty()) {
                        Address shelterLocationAddress = shelterAddresses.get(0);
                        LatLng shelterLatLng = new LatLng(shelterLocationAddress.getLatitude(), shelterLocationAddress.getLongitude());
                        float[] results = new float[1];
                        Location.distanceBetween(userLocation.latitude, userLocation.longitude, shelterLatLng.latitude, shelterLatLng.longitude, results);
                        float distance = results[0];
                        if (distance < nearestDistance) {
                            nearestDistance = distance;
                            nearestShelterLocation = shelterLatLng;
                            nearestShelterInfo = shelterName;
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Geocoder failed to get location for shelter", e);
                }
            }

            if (nearestShelterLocation != null) {
                // 마커를 추가
                mMap.addMarker(new MarkerOptions().position(nearestShelterLocation).title(nearestShelterInfo));

                // 사용자 위치와 가장 가까운 대피소 사이의 경로를 그리는 메서드를 호출합니다.
                drawRoute(userLocation, nearestShelterLocation);
            }
        }
    }
    private void drawRoute(LatLng origin, LatLng destination) {
        // Google Directions API를 사용하여 경로를 구하고 지도에 표시하는 로직
        DirectionsApiRequest request = DirectionsApi.newRequest(mGeoApiContext)
                .mode(TravelMode.TRANSIT) // 사용자의 요구에 맞는 모드를 선택합니다.
                .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude));

        request.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                // API 응답이 성공적으로 반환되면, 지도에 경로를 그립니다.
                runOnUiThread(() -> {
                    if (result.routes != null && result.routes.length > 0) {
                        DirectionsRoute route = result.routes[0];
                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.BLUE);
                        polylineOptions.width(10);

                        for (com.google.maps.model.LatLng point : route.overviewPolyline.decodePath()) {
                            polylineOptions.add(new LatLng(point.lat, point.lng));
                        }

                        mMap.addPolyline(polylineOptions);
                    }
                });
            }

            @Override
            public void onFailure(Throwable e) {
                // 요청이 실패하면 오류 로그를 출력합니다.
                Log.e("DirectionsAPI", "Error with Directions API", e);
            }
        });
        // 지도가 준비되면 로딩 화면 숨기기
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 권한 확인
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 권한이 있을 경우, 내 위치 표시 활성화
            mMap.setMyLocationEnabled(true);
            getDeviceLocation();
        } else {
            // 권한이 없을 경우, 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public void setmDatabase(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }
}
