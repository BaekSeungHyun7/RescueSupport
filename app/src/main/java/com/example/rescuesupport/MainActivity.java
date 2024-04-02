package com.example.rescuesupport;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DrawerHelper drawerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //개발용 테스트 키해시 로그
        KeyHashGenerator.generateKeyHash(this);

        /*
        //툴바 및 로그인 드로우 Layout 제어
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_userinterface_resize);
        DrawerHelper drawerHelper = new DrawerHelper(this, drawerLayout, toolbar);
        */

        //바텀 메뉴 기능
        BottomNavigationHelper.setupBottomNavigation(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerHelper.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}