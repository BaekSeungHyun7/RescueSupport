package com.example.rescuesupport;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_menu_weather extends Fragment {

    Context context;
    TextView tv_name, tv_country;
    ImageView iv_weather;
    TextView tv_temp, tv_main, tv_description;
    TextView tv_wind, tv_cloud, tv_humidity;
    APIService apiInterface = null;

    private RecyclerView weatherRecyclerView;
    // 예보지점 X 좌표
    // 예보지점 Y 좌표


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_weather, container, false);
        context = getActivity();

        initView(view);
        requestNetwork();

        TextView tvDate = view.findViewById(R.id.tvDate);
        weatherRecyclerView = view.findViewById(R.id.weatherRecyclerView);
        Button btnRefresh = view.findViewById(R.id.btnRefresh);

        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        tvDate.setText(new SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(Calendar.getInstance().getTime()) + "날씨");

        setWeather();

        btnRefresh.setOnClickListener(v -> setWeather());

        return view;
    }

    /* view 를 설정하는 메소드 */
    private void initView(View view) {
        tv_name = view.findViewById(R.id.tv_name);
        tv_country = view.findViewById(R.id.tv_country);
        iv_weather = view.findViewById(R.id.iv_weather);
        tv_temp = view.findViewById(R.id.tv_temp);
        tv_main = view.findViewById(R.id.tv_main);
        tv_description = view.findViewById(R.id.tv_description);
        tv_wind = view.findViewById(R.id.tv_wind);
        tv_cloud = view.findViewById(R.id.tv_cloud);
        tv_humidity = view.findViewById(R.id.tv_humidity);

    }

    /* retrofit 을 통해 통신을 요청하기 위한 메소드 */
    private void requestNetwork() {
        // 로딩 다이얼로그 표시
        BottomNavigationHelper.showLoadingDialog();

        // retrofit 객체와 인터페이스 연결
        apiInterface = APIClient.getClient(getString(R.string.weather_url)).create(APIService.class);

        // 통신 요청
        Call<WeatherInfoModel> call = apiInterface.doGetJsonData("weather", "Yongin", getString(R.string.weather_app_id));

        // 응답 콜백 구현
        call.enqueue(new Callback<WeatherInfoModel>() {


            @Override
            public void onResponse(@NonNull Call<WeatherInfoModel> call, @NonNull Response<WeatherInfoModel> response) {
                WeatherInfoModel resource = response.body();
                // 로딩 다이얼로그 숨기기
                BottomNavigationHelper.hideLoadingDialog();

                if (response.isSuccessful()) {
                    assert resource != null;
                    setWeatherData(resource);  // UI 업데이트
                } else {
                    showFailPop();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherInfoModel> call, @NonNull Throwable t) {
                call.cancel();
                // 로딩 다이얼로그 숨기기
                BottomNavigationHelper.hideLoadingDialog();
                showFailPop();
            }

        });
    }

    /* 통신하여 받아온 날씨 데이터를 통해 UI 업데이트 메소드 */
    @SuppressLint("SetTextI18n")
    private void setWeatherData(WeatherInfoModel model) {
        tv_name.setText(model.getName());
        tv_country.setText(model.getSys().getCountry());
        Glide.with(context).load(getString(R.string.weather_url) + "img/w/" + model.getWeather().get(0).getIcon() + ".png")  // Glide 라이브러리를 이용하여 ImageView 에 url 로 이미지 지정
                .placeholder(R.drawable.gl_lv_bug_resize)
                .error(R.drawable.gl_lv_bug_resize)
                .into(iv_weather);
        tv_temp.setText(doubleToStrFormat(model.getMain().getTemp() - 273.15) + " 'C");  // 소수점 2번째 자리까지 반올림하기
        tv_main.setText(model.getWeather().get(0).getMain());
        tv_description.setText(model.getWeather().get(0).getDescription());
        tv_wind.setText(doubleToStrFormat(model.getWind().getSpeed()) + " m/s");
        tv_cloud.setText(doubleToStrFormat(model.getClouds().getAll()) + " %");
        tv_humidity.setText(doubleToStrFormat(model.getMain().getHumidity()) + " %");
    }

    /* 통신 실패시 AlertDialog 표시하는 메소드 */
    private void showFailPop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Title").setMessage("통신실패");

        builder.setPositiveButton("OK", (dialog, id) -> Toast.makeText(context, "OK Click", Toast.LENGTH_SHORT).show());

        builder.setNegativeButton("Cancel", (dialog, id) -> Toast.makeText(context, "Cancel Click", Toast.LENGTH_SHORT).show());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /* 소수점 n번째 자리까지 반올림하기 */
    @SuppressLint("DefaultLocale")
    private String doubleToStrFormat(double value) {
        return String.format("%." + 2 + "f", value);
    }

    private void setWeather() {
        Calendar cal = Calendar.getInstance();
        // 발표 일자
        String base_date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.getTime());
        String timeH = new SimpleDateFormat("HH", Locale.getDefault()).format(cal.getTime());
        String timeM = new SimpleDateFormat("mm", Locale.getDefault()).format(cal.getTime());
        // 발표 시각
        String base_time = getBaseTime(timeH, timeM);
        if ("00".equals(timeH) && "2330".equals(base_time)) {
            cal.add(Calendar.DATE, -1);
            base_date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.getTime());
        }

        Call<WEATHER> call = ApiObject.getRetrofitService().GetWeather(60, 1, "JSON", base_date, base_time, "55", "127");
        call.enqueue(new Callback<WEATHER>() {
            @Override
            public void onResponse(@NonNull Call<WEATHER> call, @NonNull Response<WEATHER> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<ITEM> it = response.body().response.body.items.item;
                    ModelWeather[] weatherArr = new ModelWeather[6];
                    for (int i = 0; i < 6; i++) {
                        weatherArr[i] = new ModelWeather();
                    }

                    int index = 0;
                    int totalCount = response.body().response.body.totalCount - 1;
                    for (int i = 0; i <= totalCount; i++) {
                        index %= 6;
                        switch (it.get(i).category) {
                            case "PTY":
                                weatherArr[index].rainType = it.get(i).fcstValue;
                                break;
                            case "REH":
                                weatherArr[index].humidity = it.get(i).fcstValue;
                                break;
                            case "SKY":
                                weatherArr[index].sky = it.get(i).fcstValue;
                                break;
                            case "T1H":
                                weatherArr[index].temp = it.get(i).fcstValue;
                                break;
                            default:
                                continue;
                        }
                        index++;
                    }

                    for (int i = 0; i < 6; i++) {
                        weatherArr[i].fcstTime = it.get(i).fcstTime;
                    }

                    weatherRecyclerView.setAdapter(new WeatherAdapter(weatherArr));

                    Toast.makeText(context, it.get(0).fcstDate + ", " + it.get(0).fcstTime + "의 날씨 정보입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(@NonNull Call<WEATHER> call, @NonNull Throwable t) {
                TextView tvError = requireView().findViewById(R.id.tvError);
                tvError.setText("api fail : " + t.getMessage() + "\n 다시 시도해주세요.");
                tvError.setVisibility(View.VISIBLE);
                Log.d("api fail", t.getMessage());
            }
        });
    }

    private String getBaseTime(String h, String m) {
        String result;

        if (Integer.parseInt(m) < 45) {
            if ("00".equals(h)) {
                result = "2330";
            } else {
                int resultH = Integer.parseInt(h) - 1;
                result = (resultH < 10 ? "0" + resultH : resultH) + "30";
            }
        } else {
            result = h + "30";
        }

        return result;
    }

}

