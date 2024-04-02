package com.example.rescuesupport;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.AgeRange;
import com.kakao.sdk.user.model.Gender;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class KakaoLogin extends AppCompatActivity {

    private static final String TAG = "KakaoLogin";
    private View loginButton, logoutButton;
    private TextView nickName;
    private TextView gender;
    private TextView age;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        loginButton = findViewById(R.id.login);
        logoutButton = findViewById(R.id.logout);
        nickName = findViewById(R.id.nickname);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        profileImage = findViewById(R.id.profile);

        // 카카오톡이 설치되어 있는지 확인하는 메서드 , 카카오에서 제공함. 콜백 객체를 이용합.
        // 콜백 메서드 ,
        Function2<OAuthToken,Throwable,Unit> callback = (oAuthToken, throwable) -> {
            Log.e(TAG,"CallBack Method");
            //oAuthToken != null 이라면 로그인 성공
            if(oAuthToken!=null){
                // 토큰이 전달된다면 로그인이 성공한 것이고 토큰이 전달되지 않으면 로그인 실패한다.
                updateKakaoLoginUi();

            }else {
                //로그인 실패
                Log.e(TAG, "invoke: login fail" );
            }

            return null;
        };

        // 로그인 버튼 클릭 리스너
        loginButton.setOnClickListener(view -> {

            // 해당 기기에 카카오톡이 설치되어 있는 확인
            if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(KakaoLogin.this)){
                UserApiClient.getInstance().loginWithKakaoTalk(KakaoLogin.this, callback);
            }else{
                // 카카오톡이 설치되어 있지 않다면
                UserApiClient.getInstance().loginWithKakaoAccount(KakaoLogin.this, callback);
            }
        });

        // 로그아읏 버튼
        logoutButton.setOnClickListener(view -> UserApiClient.getInstance().logout(throwable -> {
            updateKakaoLoginUi();
            return null;
        }));

        updateKakaoLoginUi();
    }

    private void updateKakaoLoginUi() {

        // 로그인 여부에 따른 UI 설정
        UserApiClient.getInstance().me((user, throwable) -> {

            if (user != null) {

                // 유저 추가정보 동의 1 성별 2 나이대
                List<String> scopes = new ArrayList<>();
                if (user.getKakaoAccount() != null && Boolean.TRUE.equals(user.getKakaoAccount().getGenderNeedsAgreement())) {
                    scopes.add("gender");
                }
                if (user.getKakaoAccount() != null && Boolean.TRUE.equals(user.getKakaoAccount().getAgeRangeNeedsAgreement())) {
                    scopes.add("age_range");
                }

                if (scopes.size() > 0) {
                    Log.d(TAG, "사용자에게 추가 동의를 받아야 합니다.");

                    UserApiClient.getInstance().loginWithNewScopes(KakaoLogin.this, scopes, "KAKAOACCOUNT", (token, error) -> {
                        if (error != null) {
                            Log.e(TAG, "사용자 추가 동의 실패", error);
                        } else {
                            Log.d(TAG, "allowed scopes: " + token.getScopes());

                            // 사용자 정보 재요청
                            UserApiClient.getInstance().me((user1, error1) -> {
                                if (error1 != null) {
                                    Log.e(TAG, "사용자 정보 요청 실패", error1);
                                } else if (user1 != null) {
                                    Log.i(TAG, "사용자 정보 요청 성공");
                                    // 여기서 다시 사용자 정보 업데이트를 처리할 수 있습니다.
                                }
                                return null;
                            });
                        }
                        return null;
                    });
                }

                // 유저의 아이디
                Log.d(TAG, "invoke: id =" + user.getId());
                // 유저의 이메일
                assert user.getKakaoAccount() != null;
                Log.d(TAG, "invoke: email =" + user.getKakaoAccount().getEmail());
                // 유저의 닉네임
                assert user.getKakaoAccount().getProfile() != null;
                Log.d(TAG, "invoke: nickname =" + user.getKakaoAccount().getProfile().getNickname());
                // 유저의 성별
                Log.d(TAG, "invoke: gender =" + user.getKakaoAccount().getGender());
                // 유저의 연령대
                Log.d(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());

                // 유저 닉네임 세팅
                nickName.setText(user.getKakaoAccount().getProfile().getNickname());

                // 유저 성별 세팅
                Gender userGender = user.getKakaoAccount().getGender();
                if(userGender != null) {
                    gender.setText(userGender.toString());
                }

                // 유저 나이대 세팅
                AgeRange userAgeRange = user.getKakaoAccount().getAgeRange();
                if(userAgeRange != null) {
                    age.setText(userAgeRange.toString());
                }

                // 유저 프로필 사진 세팅
                Glide.with(profileImage).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(profileImage);
                Log.d(TAG, "invoke: profile = "+user.getKakaoAccount().getProfile().getThumbnailImageUrl());

                // 유저 정보 수행 관련 : SharedPreferences 유저 닉네임 저장
                SharedPreferences sharedPref = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("USER_NICKNAME", user.getKakaoAccount().getProfile().getNickname());
                editor.apply();

                // 로그인이 되어있으면
                loginButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.VISIBLE);


            } else {
                // 로그인 되어있지 않으면
                nickName.setText(null);
                profileImage.setImageBitmap(null);

                loginButton.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.GONE);
            }
            return null;
        });
    }

}

