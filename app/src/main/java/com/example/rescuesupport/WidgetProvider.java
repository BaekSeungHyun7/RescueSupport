package com.example.rescuesupport;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // 위젯 레이 아웃 설정.
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_provider);

        //Test_number 값
        String testNumber = context.getString(R.string.Test_number);

        // btn_emergency_call 버튼을 클릭 했을 때 바로 전화 걸기.
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + testNumber));
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent callPendingIntent = PendingIntent.getActivity(context, 0, callIntent, 0);
        views.setOnClickPendingIntent(R.id.btn_emergency_call, callPendingIntent);

        // btn_sendMessage 클릭
        Intent sendMessageIntent = new Intent(context, WidgetSendMessage.class);
        sendMessageIntent.setAction("com.example.rescuesupport.ACTION_WIDGET_SEND_MESSAGE");
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent sendMessagePendingIntent = PendingIntent.getBroadcast(context, 1, sendMessageIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btn_sendmessage, sendMessagePendingIntent);


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

}

