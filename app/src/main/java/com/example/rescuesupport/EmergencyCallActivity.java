package com.example.rescuesupport;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class EmergencyCallActivity extends AppCompatActivity {

    private final String Test_number = "tel:01090483223";
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);

        Button callAllButton = findViewById(R.id.btn_call_all);
        Button call112Button = findViewById(R.id.btn_call_112);
        Button call119Button = findViewById(R.id.btn_call_119);
        Button call110Button = findViewById(R.id.btn_call_110);
        Button calletcButton = findViewById(R.id.btn_call_etc);


        callAllButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(EmergencyCallActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EmergencyCallActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
            } else {
                sendSMS();
            }
        }); //메시지 발송

        call112Button.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(EmergencyCallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EmergencyCallActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(Test_number));
                startActivity(callIntent);
            }
        });//112

        call119Button.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(EmergencyCallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EmergencyCallActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(Test_number));
                startActivity(callIntent);
            }
        });//119

        call110Button.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(EmergencyCallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EmergencyCallActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(Test_number));
                startActivity(callIntent);
            }
        });//110

        calletcButton.setOnClickListener(v -> {});//기타 전화 기능 보기

    }
    @SuppressLint("UnlocalizedSms")
    private void sendSMS() {

        //사용자 정보
        SharedPreferences sharedPref = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        String userNickname = sharedPref.getString("USER_NICKNAME", "Unknown User");
        String userGender = sharedPref.getString("GENDER", "Unknown Gender");
        String userAge = sharedPref.getString("AGE", "Unknown Age");
        String userMemo = sharedPref.getString("MEMO", "Unknown Memo");

        //사용자 위치
        String userLocation = getCurrentLocationString();

        //문자 발송용 메시지 구성
        String message = "[긴급 문자 발송]\n" +
                "사용자 정보 : " + userNickname + " / " + userGender + " / " + userAge + "\n" +
                "현재 위치 : " + userLocation + "\n" +
                "특이 사항 : " + userMemo + "\n";

        SmsManager smsManager = SmsManager.getDefault();

        //가상머신 SMS 글자제한으로 인한 메시지 분할용 코드
        ArrayList<String> messageParts = smsManager.divideMessage(message);
        for (String part : messageParts) {
            smsManager.sendTextMessage(Test_number, null, part, null, null);
        }

        try {
            smsManager.sendTextMessage("tel:01090483223", null, message, null, null);
            Toast.makeText(this, "메시지 발송 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "메시지 발송 실패", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // SharedPreferences를 사용하여 위젯용 사용자 정보 저장
        SharedPreferences sharedPrefLocation = getSharedPreferences("USER_Location_Widget", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefLocation.edit();
        editor.putString("userLocationForWidget", userLocation);
        editor.apply();
    }

    private String getCurrentLocationString() {
        Location location = getCurrentLocation();
        if (location != null) {
            return getAddressFromLocation(location.getLatitude(), location.getLongitude());
        } else {
            return "위치 정보 획득 실패";
        }
    }

    private Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return null;
        }
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (getCurrentLocation() != null) {
                    sendSMS();
                } else {
                    Toast.makeText(this, "위치 정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressText = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressText.append(address.getAddressLine(i)).append("\n");
                }
                return addressText.toString().trim();
            } else {
                return "주소를 찾을 수 없습니다.";
            }
        } catch (IOException e) {
            return "Geocoder 서비스 사용 중 오류";
        }
    }
}



