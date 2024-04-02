package com.example.rescuesupport;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class MainTableUserInfoActivity extends AppCompatActivity {
    private EditText editTextMemo;

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

        //카카오 로그인 관련
        loginButton = findViewById(R.id.login);
        logoutButton = findViewById(R.id.logout);
        nickName = findViewById(R.id.nickname);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        
        //저장된 사용자 정보
        profileImage = findViewById(R.id.profile);
        editTextMemo = findViewById(R.id.editTextMemo);
        // SharedPreferences에서 저장된 사용자 정보 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String memo = sharedPreferences.getString("Memo", "");
        // 불러온 정보를 EditText에 표시
        editTextMemo.setText(memo);
        loginButton = findViewById(R.id.login);
        logoutButton = findViewById(R.id.logout);
        nickName = findViewById(R.id.nickname);
        profileImage = findViewById(R.id.profile);

        // 카카오톡이 설치되어 있는지 확인하는 메서드
        // 콜백 메서드 ,
        Function2<OAuthToken,Throwable,Unit> callback = (oAuthToken, throwable) -> {
            Log.e(TAG,"CallBack Method");
            //oAuthToken != null 이라면 로그인 성공
            if(oAuthToken!=null){
                // 토큰이 전달된다면 로그인이 성공한 것이고 토큰이 전달되지 않으면 로그인 실패한다.
                updateKakaoLoginUi();

            }else {
                //로그인 실패
                Log.e(TAG, "invoke: login fail", throwable);

            }

            return null;
        };

        // 로그인 버튼 클릭 리스너
        loginButton.setOnClickListener(view -> {

            // 해당 기기에 카카오톡이 설치되어 있는 확인
            if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainTableUserInfoActivity.this)){
                UserApiClient.getInstance().loginWithKakaoTalk(MainTableUserInfoActivity.this, callback);
            }else{
                // 카카오톡이 설치되어 있지 않다면
                UserApiClient.getInstance().loginWithKakaoAccount(MainTableUserInfoActivity.this, callback);
            }
        });

        // 로그아읏 버튼
        logoutButton.setOnClickListener(view -> UserApiClient.getInstance().logout(throwable -> {
            updateKakaoLoginUi();
            return null;
        }));

        updateKakaoLoginUi();

    }

    public void onSaveButtonClick(View view) {
        String memo = editTextMemo.getText().toString();
        String savedNickName = nickName.getText().toString().replace("이름 : ", "");
        String savedGender = gender.getText().toString().replace("성별 : ", "");
        String savedAge = age.getText().toString().replace("나이(연령대) : ", "");

        // SharedPreferences를 사용하여 사용자 정보 저장
        SharedPreferences sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("MEMO", memo);
        editor.putString("USER_NICKNAME", savedNickName);
        editor.putString("GENDER", savedGender);
        editor.putString("AGE", savedAge);

        editor.apply();

        // 사용자에게 저장이 완료되었다는 메시지를 표시
        Toast.makeText(this, "사용자 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show();
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

                    UserApiClient.getInstance().loginWithNewScopes(MainTableUserInfoActivity.this, scopes, "KAKAOACCOUNT", (token, error) -> {
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
                                    // 여기서 다시 사용자 정보 업데이트를 처리 -> 10/2 고쳐야함
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
                String formattedName = "이름 : " + user.getKakaoAccount().getProfile().getNickname();
                nickName.setText(formattedName);
                // 유저 프로필 사진 세팅
                Glide.with(profileImage).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(profileImage);
                Log.d(TAG, "invoke: profile = "+user.getKakaoAccount().getProfile().getThumbnailImageUrl());
                // 유저 성별 세팅
                assert user.getKakaoAccount().getGender() != null;
                String formattedGender = "성별 : " + getReadableGender(user.getKakaoAccount().getGender().toString());
                gender.setText(formattedGender);

                // 유저 나이대 세팅
                assert user.getKakaoAccount().getAgeRange() != null;
                String formattedAge = "나이(연령대) : " + getReadableAgeRange(user.getKakaoAccount().getAgeRange().toString());
                age.setText(formattedAge);


                // 로그인이 되어있으면
                loginButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.VISIBLE);
            } else {
                // 로그인 되어있지 않으면
                nickName.setText(null);
                gender.setText(null);
                age.setText(null);
                profileImage.setImageBitmap(null);

                loginButton.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.GONE);
            }
            return null;
        });

    }

    private String getReadableGender(String gender) {
        switch (gender) {
            case "MALE":
                return "남성";
            case "FEMALE":
                return "여성";
            default:
                return gender; // 기본값은 원래 문자열을 그대로 반환
        }
    }

    private String getReadableAgeRange(String ageRange) {
        switch (ageRange) {
            case "AGE_00_09":
                return "미취학 아동";
            case "AGE_10_19":
                return "10대";
            case "AGE_20_29":
                return "20대";
            case "AGE_30_39":
                return "30대";
            case "AGE_40_49":
                return "40대";
            case "AGE_50_59":
                return "50대";
            case "AGE_60_69":
                return "60대";
            case "AGE_70_79":
                return "70대";
            case "AGE_80_89":
                return "80대";
            case "AGE_90_99":
                return "90대";
            default:
                return ageRange; // 기본값은 원래 문자열을 그대로 반환 -> 오류?
        }
    }

}


