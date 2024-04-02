package com.example.rescuesupport;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class MapButtonClickListener implements View.OnClickListener {
    private final Context context;

    public MapButtonClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, Fragment_search_shelter.class);
        context.startActivity(intent);
    }
}
