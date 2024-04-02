package com.example.rescuesupport;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class TabFragmentSelector {
    private final Fragment[] fragments;

    public TabFragmentSelector(Fragment... fragments) {
        this.fragments = fragments;
    }

    public void selectTabFragment(int position, FragmentTransaction transaction) {
        Fragment selectedFragment = fragments[position];
        transaction.replace(R.id.container, selectedFragment);
        transaction.commit();
    }
}
