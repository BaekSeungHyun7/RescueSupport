package com.example.rescuesupport;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class CurrentLocationHelper implements LocationListener {

    private Context context;
    private LocationManager locationManager;
    private TextView currentLocationTextView;

    public CurrentLocationHelper(Context context, TextView currentLocationTextView) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.currentLocationTextView = currentLocationTextView;
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void stopLocationUpdates() {
        locationManager.removeUpdates(this);
    }

    public void refreshLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
    }

    public String getAddressFromCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.KOREAN); // 한국어 설정
        List<Address> addresses;
        String address = "";

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                Address currentAddress = addresses.get(0);
                address = currentAddress.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        String address = getAddressFromCoordinates(latitude, longitude);

        // 텍스트 뷰에 현재 위치 정보를 설정
        currentLocationTextView.setText(address);

        //텍스트 색
        currentLocationTextView.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // 위치 제공자 상태 변경 시 호출됨
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        // 위치 제공자 활성화 시 호출됨
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        // 위치 제공자 비활성화 시 호출됨
    }

    public String getCurrentAddress() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "Location permission not granted";
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            return getAddressFromCoordinates(latitude, longitude);
        } else {
            return "Location not available";
        }
    }


}
