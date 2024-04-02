package com.example.rescuesupport;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Fragment_search_shelter extends AppCompatActivity {

    private Spinner spinnerSubLocation, spinnerShelterType, spinnerLocation; // 스피너 선언
    private Button mCurrentLocationButton, mSearchButton, buttonSort; // 버튼 선언
    private ListView mListView; // 리스트뷰 선언
    private Dialog loadingDialog; // 로딩 다이얼로그 선언
    private ShelterArrayAdapter shelterAdapter; // 커스텀 어댑터 선언
    private FusedLocationProviderClient fusedLocationClient; // 위치 제공자 클라이언트 선언
    private List<String> itemList; // 리스트 아이템을 담을 리스트 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_shelter);

        initializeViews(); // 뷰 초기화
        setupFusedLocationProvider(); // 위치 제공자 설정
        setupSpinners(); // 스피너 설정
        setupListView(); // 리스트 뷰 설정
        setupButtons(); // 버튼 설정
    }

    private void initializeViews() {
        spinnerLocation = findViewById(R.id.spinner_location);
        spinnerSubLocation = findViewById(R.id.spinner_sub_location);
        spinnerShelterType = findViewById(R.id.spinner_shelter_type);
        mCurrentLocationButton = findViewById(R.id.button_current_location);
        mListView = findViewById(R.id.shelter_list_view);
        mSearchButton = findViewById(R.id.button_shelter_result);
        buttonSort = findViewById(R.id.button_sort);
    }

    private void setupFusedLocationProvider() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.shelter_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShelterType.setAdapter(adapter);

        spinnerLocation.setOnItemSelectedListener(new LocationItemSelectedListener(this));
        spinnerShelterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택된 항목 처리
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 선택하지 않았을 때 처리
            }
        });
    }

    private void setupListView() {
        itemList = new ArrayList<>();
        shelterAdapter = new ShelterArrayAdapter(this, itemList);
        mListView.setAdapter(shelterAdapter);
    }

    private void setupButtons() {
        mCurrentLocationButton.setOnClickListener(v -> navigateToCurrentLocation());
        mSearchButton.setOnClickListener(v -> performSearch());
        buttonSort.setOnClickListener(v -> sortList());
    }

    private void navigateToCurrentLocation() {
        startActivity(new Intent(Fragment_search_shelter.this, MapCurrentLocation.class));
    }

    private void performSearch() {
        showLoadingDialog();
        new Handler().postDelayed(this::updateListView, 1500);
    }

    private void updateListView() {
        String shelterType = spinnerShelterType.getSelectedItem().toString();
        String location1 = spinnerLocation.getSelectedItem().toString();
        String location2 = spinnerSubLocation.getSelectedItem().toString().replace(" ", "_");
        int resId = getResources().getIdentifier("shelter_" + location1 + "_" + location2 + "_" + shelterType, "array", getPackageName());
        String[] items;

        try {
            items = getResources().getStringArray(resId);
        } catch (Resources.NotFoundException e) {
            items = new String[] {"해당 조건에 맞는 대피소 정보가 없습니다."};
        }

        itemList.clear();
        itemList.addAll(Arrays.asList(items));
        shelterAdapter.notifyDataSetChanged();

        mListView.setOnItemClickListener((parent, view, position, id) -> openFragmentGetDir((String) parent.getItemAtPosition(position)));
        hideLoadingDialog();
    }

    private void sortList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("정렬 방식 선택")
                .setItems(new String[]{"이름순 정렬", "거리순 정렬"}, this::sortOptionsHandler)
                .show();
    }

    private void sortOptionsHandler(DialogInterface dialog, int which) {
        if (which == 0) {
            Collections.sort(itemList, String.CASE_INSENSITIVE_ORDER);
            shelterAdapter.notifyDataSetChanged();
        } else {
            // 거리순 정렬 로직 추가 예정
        }
    }

    private void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new Dialog(this);
            loadingDialog.setContentView(R.layout.loading_dialog_apicallback);
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadingDialog.setCancelable(false);
        }
        loadingDialog.show();
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private void openFragmentGetDir(String selectedItem) {
        if (selectedItem != null) {
            Intent intent = new Intent(this, GetDirections.class);
            intent.putExtra("selectedItem", selectedItem);
            startActivity(intent);
        } else {
            Toast.makeText(this, "선택된 항목이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public Spinner getSpinnerSubLocation() {
        return spinnerSubLocation;
    }
}












