package com.example.rescuesupport;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class WidgetSendMessage extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("com.example.rescuesupport.ACTION_WIDGET_SEND_MESSAGE")) {
            Log.d("WidgetSendMessage", "성공");
            sendSmsMessage(context);
        }
    }

    @SuppressLint("UnlocalizedSms")
    private void sendSmsMessage(Context context) {
        // SmsManager 객체를 가져옴
        SmsManager smsManager = SmsManager.getDefault();

        // 사용자 정보
        SharedPreferences sharedPref = context.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        String userNickname = sharedPref.getString("USER_NICKNAME", "Unknown User");
        String userGender = sharedPref.getString("GENDER", "Unknown Gender");
        String userAge = sharedPref.getString("AGE", "Unknown Age");
        String userMemo = sharedPref.getString("MEMO", "Unknown Memo");

        // 사용자 위치 정보
        SharedPreferences sharedPrefLocation = context.getSharedPreferences("USER_Location_Widget", Context.MODE_PRIVATE);
        String userLocation = sharedPrefLocation.getString("userLocationForWidget", "Unknown Location");

        // 문자 발송용 메시지 구성
        String formattedMessage = "[긴급 문자 발송]\n" +
                "사용자 정보 : " + userNickname + " / " + userGender + " / " + userAge + "\n" +
                "현재 위치 : " + userLocation + "\n" +
                "특이 사항 : " + userMemo + "\n";

        // 가상 머신 SMS 글자 제한 메시지 분할용 코드
        ArrayList<String> messageParts = smsManager.divideMessage(formattedMessage);
        for (String part : messageParts) {
            String test_number = "01090483223";
            smsManager.sendTextMessage(test_number, null, part, null, null);
        }

        try {
            smsManager.sendTextMessage("tel:01090483223", null, formattedMessage, null, null);
            Toast.makeText(context, "긴급 문자 발송", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "문자 발송 실패.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}


