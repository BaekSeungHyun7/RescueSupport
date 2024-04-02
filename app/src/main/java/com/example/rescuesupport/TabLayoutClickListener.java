package com.example.rescuesupport;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;

public class TabLayoutClickListener implements TabLayout.OnTabSelectedListener {
    private final Fragment fragmentTableNature;
    private final Fragment fragmentTableSocial;
    private final Fragment fragmentTableLiving;
    private final FragmentManager fragmentManager;

    public TabLayoutClickListener(Fragment_table_nature fragmentTableNature, Fragment_table_social fragmentTableSocial,
                                  Fragment_table_living fragmentTableLiving, FragmentManager fragmentManager) {
        this.fragmentTableNature = fragmentTableNature;
        this.fragmentTableSocial = fragmentTableSocial;
        this.fragmentTableLiving = fragmentTableLiving;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        Log.d("MainActivity", "선택된 탭 : " + position);

        Fragment selected = null;
        if (position == 0) {
            selected = fragmentTableNature;
        } else if (position == 1) {
            selected = fragmentTableSocial;
        } else if (position == 2) {
            selected = fragmentTableLiving;
        }

        assert selected != null;
        fragmentManager.beginTransaction().replace(R.id.container, selected).commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}

