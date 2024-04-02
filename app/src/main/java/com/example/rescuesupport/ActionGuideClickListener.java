package com.example.rescuesupport;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class ActionGuideClickListener implements View.OnClickListener {
    private final Context context;

    public ActionGuideClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ActionGuideActivity.class);
        context.startActivity(intent);
    }
}