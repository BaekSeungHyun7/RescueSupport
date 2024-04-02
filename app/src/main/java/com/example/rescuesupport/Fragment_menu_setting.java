package com.example.rescuesupport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Fragment_menu_setting extends Fragment implements SensorEventListener {
    private MediaPlayer mediaPlayer;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchDetect;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchDetectTest;
    private float lastX, lastY, lastZ;
    private static final int SHAKE_THRESHOLD = 600; // 기본값, 이 값을 변경 하거나 조정 메소드 통해 변경 가능
    private boolean isSystemOnMessageShown = false; // 클래스 레벨 변수
    private boolean isAlertDialogShown = false; // 경고창 상태 체크용, 무한 반복 방지
    private final Handler delayHandler = new Handler(); // 경고창 상태 닫기 -> 재생성 무한 반복 방지 딜레이 변수
    private boolean isDelayActive = false; // 딜레이 활성화 체크 플래그

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_setting, container, false);

        // 위젯 버튼 리스너
        Button btnWidgetOn = view.findViewById(R.id.btn_widget_on);
        btnWidgetOn.setOnClickListener(v -> Toast.makeText(getActivity(),
                "안드로이드 위젯 목록에서 앱 -> RS -> 위젯을 드래그하여 추가해주세요.", Toast.LENGTH_LONG).show());
        switchDetect = view.findViewById(R.id.switch_detect);
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        switchDetectTest = view.findViewById(R.id.switch_detect_test);

        switchDetectTest.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // 스위치가 켜질 때
                initializeMediaPlayer(); // MediaPlayer를 초기화하는 메소드 호출
            } else {
                // 스위치가 꺼질 때
                if (mediaPlayer != null) {
                    mediaPlayer.release(); // MediaPlayer 리소스 해제
                    mediaPlayer = null; // MediaPlayer 참조 제거
                }
            }
        });


        // 키 해시 값을 표시하는 버튼 리스너
        Button btnKeyTest = view.findViewById(R.id.btn_key_test);
        btnKeyTest.setOnClickListener(v -> {
            try {
                PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                    Toast.makeText(getActivity(), "키해시 값: " + keyHash, Toast.LENGTH_LONG).show();
                }
            } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
                Toast.makeText(getActivity(), "Error while getting Key Hash: " + e.toString(), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (switchDetect.isChecked()) { // Switch ON 상태인 경우 흔들림 감지

            if (!isSystemOnMessageShown) {
                Toast.makeText(getActivity(), "흔들림 감지 시스템 ON", Toast.LENGTH_SHORT).show();
                isSystemOnMessageShown = true; // 메시지 띄운 상태로 설정
            }

            if(switchDetectTest.isChecked() && !isAlertDialogShown) { // 테스트 스위치 ON 이고 경고 창이 아직 뜨지 않았을 경우
                showAlertDialog();
                isAlertDialogShown = true; // 경고 창이 띄워진 상태로 설정
                return;
            }


            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float deltaX = x - lastX;
                float deltaY = y - lastY;
                float deltaZ = z - lastZ;

                float acceleration = (float) (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / SensorManager.GRAVITY_EARTH - 1.0);

                if (acceleration > getShakeThreshold()) {
                    showAlertDialog();
                }

                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }else {
            // Switch가 OFF 상태이면 메시지 띄운 상태를 초기화
            isSystemOnMessageShown = false;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null && accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        delayHandler.removeCallbacksAndMessages(null);
        isDelayActive = false;
        isAlertDialogShown = false;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing here
    }
    //테스트 메서드
    private float getShakeThreshold() {
        if (switchDetectTest.isChecked()) { // Switch가 ON 상태인 경우
            return 600;
        } else {
            return SHAKE_THRESHOLD; // 원하는 기본 값
        }
    }
    private void showAlertDialog() {
        if (isDelayActive) return;

        // 경고음 재생 설정
        final MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.alert_sound);
        mediaPlayer.setLooping(true);  // 반복 재생 설정
        mediaPlayer.start();

        final int[] remainingSeconds = {30};

        // Runnable 미리 선언
        final Runnable[] updateMessageRunnable = new Runnable[1];

        final AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
                .setTitle("흔들림 사고 감지!")
                .setMessage("강한 흔들림이 감지되었습니다! \n" + remainingSeconds[0] + "초 안에 경고창을 닫지 않으실 경우 자동으로 긴급 문자가 발송 됩니다.")
                .setPositiveButton("확인", (dialog, which) -> {
                    isAlertDialogShown = false;
                    isDelayActive = true;
                    delayHandler.removeCallbacks(smsRunnable);
                    delayHandler.removeCallbacks(updateMessageRunnable[0]);
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    delayHandler.postDelayed(() -> isDelayActive = false, 10000);
                    dialog.dismiss();
                })
                .setOnDismissListener(dialog -> {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                    } catch (IllegalStateException e) {
                        // Log the exception or handle it as appropriate for your app
                    } finally {
                        mediaPlayer.release();
                        // mediaPlayer = null; // 이 부분은 제거해야 합니다. mediaPlayer가 final이면 여기서 재할당할 수 없습니다.
                    }
                })
                .show();

        updateMessageRunnable[0] = new Runnable() {
            @Override
            public void run() {
                if (remainingSeconds[0] > 0) {
                    remainingSeconds[0]--;
                    alertDialog.setMessage("강한 흔들림이 감지되었습니다! \n" + remainingSeconds[0] + "초 안에 경고창을 닫지 않으실 경우 자동으로 긴급 문자가 발송 됩니다.");
                    delayHandler.postDelayed(this, 1000);
                } else {
                    delayHandler.removeCallbacks(this);
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                        Toast.makeText(getActivity(), "긴급 정보 문자가 자동 발송되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        delayHandler.postDelayed(updateMessageRunnable[0], 1000);
        delayHandler.postDelayed(smsRunnable, 30000);
    }
    private final Runnable smsRunnable = () -> {
        //사용자 정보
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        String userNickname = sharedPref.getString("USER_NICKNAME", "Unknown User");
        String userGender = sharedPref.getString("GENDER", "Unknown Gender");
        String userAge = sharedPref.getString("AGE", "Unknown Age");
        String userMemo = sharedPref.getString("MEMO", "Unknown Memo");

        // 사용자 위치 정보
        SharedPreferences sharedPrefLocation = requireActivity().getSharedPreferences("USER_Location_Widget", Context.MODE_PRIVATE);
        String userLocation = sharedPrefLocation.getString("userLocationForWidget", "Unknown Location");

        //문자 발송용 메시지 구성
        String message = "[긴급 문자 발송]\n" +
                "사용자 정보 : " + userNickname + " / " + userGender + " / " + userAge + "\n" +
                "현재 위치 : " + userLocation + "\n" +
                "특이 사항 : " + userMemo + "\n";

        SmsManager smsManager = SmsManager.getDefault();

        //가상 머신 SMS 글자 제한 해결 메시지 분할용 코드
        ArrayList<String> messageParts = smsManager.divideMessage(message);
        for (String part : messageParts) {
            String test_number = "tel:01090483223";
            smsManager.sendTextMessage(test_number, null, part, null, null);
        }
    };
    private void initializeMediaPlayer() {
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.alert_sound);
        // 필요한 설정을 더 추가할 수 있습니다.
    }

}



