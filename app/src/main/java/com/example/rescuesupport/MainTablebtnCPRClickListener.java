package com.example.rescuesupport;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class MainTablebtnCPRClickListener implements View.OnClickListener {
    private final Context context;

    public MainTablebtnCPRClickListener(Context context) { this.context = context; }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, MainTablebtnCPRActivity.class);
        context.startActivity(intent);
    }
}