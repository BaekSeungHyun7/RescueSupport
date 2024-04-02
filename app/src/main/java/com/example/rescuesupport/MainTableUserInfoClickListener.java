package com.example.rescuesupport;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class MainTableUserInfoClickListener implements View.OnClickListener {
    private final Context context;

    public MainTableUserInfoClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, MainTableUserInfoActivity.class);
        context.startActivity(intent);
    }
}