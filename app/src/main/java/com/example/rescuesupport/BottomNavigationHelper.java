package com.example.rescuesupport;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationHelper {

    private static Dialog loadingDialog;

    @SuppressLint("NonConstantResourceId")
    public static void setupBottomNavigation(FragmentActivity activity) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottom_navigationview);
        bottomNavigationView.setSelectedItemId(R.id.tab_btn_home); // Home 메뉴를 선택하도록 설정

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.tab_btn_home:
                    showFragment(activity, R.layout.fragment_menu_home);
                    return true;
                case R.id.tab_btn_alarm:
                    showFragment(activity, R.layout.fragment_menu_alarm);
                    return true;
                case R.id.tab_btn_news:
                    showFragment(activity, R.layout.fragment_menu_news);
                    return true;
                case R.id.tab_weather:
                    showFragment(activity, R.layout.fragment_menu_weather);
                    return true;
                case R.id.tab_btn_setting:
                    showFragment(activity, R.layout.fragment_menu_setting);
                    return true;
            }
            return false;
        });

        // 초기 화면
        showFragment(activity, R.layout.fragment_menu_home);

        // 로딩 로그 초기화
        loadingDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
    }


    @SuppressLint("NonConstantResourceId")
    private static void showFragment(FragmentActivity activity, int layoutResId) {

        Fragment fragment;
        switch (layoutResId) {
            case R.layout.fragment_menu_home:
                fragment = new Fragment_menu_home();
                showLoadingDialog();
                break;
            case R.layout.fragment_menu_alarm:
                fragment = new Fragment_menu_alarm();
                showLoadingDialog();
                break;
            case R.layout.fragment_menu_news:
                fragment = new Fragment_menu_news();
                showLoadingDialog();
                break;
            case R.layout.fragment_menu_weather:
                fragment = new Fragment_menu_weather();
                showLoadingDialog();
                break;
            case R.layout.fragment_menu_setting:
                fragment = new Fragment_menu_setting();
                break;
            default:
                return;
        }

        Bundle bundle = new Bundle();
        bundle.putInt("layoutResId", layoutResId);
        fragment.setArguments(bundle);

        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.containers, fragment)
                .addToBackStack(null) // Fragment를 스택에 저장
                .commit();
    }


    static void showLoadingDialog() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    // 로딩 다이얼로그 숨기는 메소드
    public static void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

}

