package com.example.rescuesupport;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class DrawerHelper {
    private final ActionBarDrawerToggle drawerToggle;
    private final ImageView loginButton;
    private final MainActivity activity;  // MainActivity 참조를 저장하기 위한 변수 추가

    public DrawerHelper(MainActivity activity, DrawerLayout drawerLayout, Toolbar toolbar) {
        this.activity = activity;  // MainActivity 인스턴스를 저장
        drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        loginButton = activity.findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // KakaoLogin 액티비티를 시작하는 Intent를 생성하고 실행
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }
}




