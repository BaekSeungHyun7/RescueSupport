package com.example.rescuesupport;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_menu_home extends Fragment {

    public Fragment_menu_home() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_home, container, false);

        // 재난 행동 지침 버튼 클릭 리스너 설정
        ImageButton btnActionGuidelines = view.findViewById(R.id.btn_action_guidelines);
        btnActionGuidelines.setOnClickListener(new ActionGuideClickListener(getActivity()));

        // 응급 연락 버튼 클릭 리스너 설정
        ImageButton btnEmergencyCall = view.findViewById(R.id.btn_emergency_call);
        btnEmergencyCall.setOnClickListener(new EmergencyCallClickListener(getActivity()));

        // 대피소 맵 버튼 클릭 리스너 설정
        ImageButton btnSearchShelter = view.findViewById(R.id.btn_search_shelter);
        btnSearchShelter.setOnClickListener(new MapButtonClickListener(getActivity()));

        // 즐겨찾기 1 클릭 리스너 설정
        Button userInfoBtn = view.findViewById(R.id.userInfoBtn);
        userInfoBtn.setOnClickListener(new MainTableUserInfoClickListener(getActivity()));

        // 즐겨찾기 2 CPR 클릭 리스너 설정
        Button btnCPR = view.findViewById(R.id.btn_CPR);
        btnCPR.setOnClickListener(new MainTablebtnCPRClickListener(getActivity()));

        // 즐겨찾기 3 가까운 대피소 버튼 클릭 리스너 설정
        Button btnNearShelter = view.findViewById(R.id.btn_near_shelter);
        btnNearShelter.setOnClickListener(new NearShelterClickListener(getActivity()));

        // 즐겨찾기 4 제세동기 클릭 리스너
        Button btnAED = view.findViewById(R.id.btn_AED);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        btnAED.setOnClickListener(new AEDClickListener(getActivity(), locationManager));

        // 즐겨찾기 5 민간 구급차 클릭 리스너
        //Button btnAmbulance = view.findViewById(R.id.btn_Private_Ambulance);
        //btnAmbulance.setOnClickListener(new AmbulanceClickListener(getActivity()));

        // 로딩 완료 후 로딩 다이얼로그 숨기기
        BottomNavigationHelper.hideLoadingDialog();

        return view;
    }
}
