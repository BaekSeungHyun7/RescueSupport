package com.example.rescuesupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    Fragment_table_nature fragment_table_nature;
    Fragment_table_social fragment_table_social;
    Fragment_table_living fragment_table_living;
    Fragment_menu_home homeFragment;
    Fragment_menu_news infoFragment;
    Fragment_menu_setting settingFragment;
    private Button btn_search_shelter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment_table_nature = new Fragment_table_nature();
        fragment_table_social = new Fragment_table_social();
        fragment_table_living = new Fragment_table_living();
        homeFragment = new Fragment_menu_home();
        infoFragment = new Fragment_menu_news();
        settingFragment = new Fragment_menu_setting();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_table_nature).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();



        //맵 기능 키 해시 얻기
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("키해시는 :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //맵 버튼 기능
        btn_search_shelter = findViewById(R.id.btn_search_shelter);
        btn_search_shelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Switch_shelter_page.class);
                startActivity(intent);
            }
        });
        // 탭 레이아웃에 탭 추가
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("자연 재해"));
        tabs.addTab(tabs.newTab().setText("사회 재난"));
        tabs.addTab(tabs.newTab().setText("생활 안전"));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("MainActivity", "선택된 탭 : "+position);

                Fragment selected = null;
                if (position == 0) {
                    selected = fragment_table_nature;
                } else if (position == 1) {
                    selected = fragment_table_social;
                } else if (position == 2) {
                    selected = fragment_table_living;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.tab_btn_home:
                        Log.d("Bottom", "연결 테스트");
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
                        return true;
                    case R.id.tab_btn_news:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, settingFragment).commit();
                        return true;
                    case R.id.tab_btn_setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, infoFragment).commit();
                        return true;
                }
                return false;
            }
        });


    }


    // 툴바의 버튼 클릭 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}