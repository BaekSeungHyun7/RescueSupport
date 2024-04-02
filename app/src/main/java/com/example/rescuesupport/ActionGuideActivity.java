package com.example.rescuesupport;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

public class ActionGuideActivity extends AppCompatActivity {
    private TabFragmentSelector tabFragmentSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionguide);

        Fragment_table_nature fragment_table_nature = new Fragment_table_nature();
        Fragment_table_social fragment_table_social = new Fragment_table_social();
        Fragment_table_living fragment_table_living = new Fragment_table_living();

        tabFragmentSelector = new TabFragmentSelector(fragment_table_nature, fragment_table_social, fragment_table_living);

        // Layout 탭 추가
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("자연 재해"));
        tabs.addTab(tabs.newTab().setText("사회 재난"));
        tabs.addTab(tabs.newTab().setText("생활 안전"));

        // 초기 선택 탭에 맞는 테이블 fragment 표시
        selectTabFragment(tabs.getSelectedTabPosition());

        // TabLayoutClickListener 추가
        tabs.addOnTabSelectedListener(new TabLayoutClickListener(fragment_table_nature,
                fragment_table_social, fragment_table_living, getSupportFragmentManager()));

    }

    private void selectTabFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        tabFragmentSelector.selectTabFragment(position, transaction);
    }
}
