package com.example.rescuesupport;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class EmergencyCallClickListener implements View.OnClickListener {
    private final Context context;

    public EmergencyCallClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, EmergencyCallActivity.class);
        context.startActivity(intent);
    }
}