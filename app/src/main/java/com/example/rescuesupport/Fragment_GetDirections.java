package com.example.rescuesupport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_GetDirections extends Fragment {

    public static Fragment_GetDirections newInstance(String shelterType) {
        Fragment_GetDirections fragment = new Fragment_GetDirections();
        Bundle args = new Bundle();
        args.putString("shelterType", shelterType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_get_directions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 구글 맵 관련 코드 매서드 따로 빼서
    }
}
