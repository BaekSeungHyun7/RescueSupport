package com.example.rescuesupport;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

// 결과 xml 파일에 접근해서 정보 가져오기
public interface WeatherInterface {
    // getUltraSrtFcst : 초단기 예보 조회 + 인증키
    @GET("getUltraSrtFcst?serviceKey=f%2FmOzdZ3OiF%2FIkC6EUBlPl5Axz7TFj25S4n%2FzxACL9HL7%2BkRrJe40j5ZyKgvLDr9pt2DvTBWy2w%2Fk9wddt46og%3D%3D")
    Call<WEATHER> GetWeather(
            @Query("numOfRows") int num_of_rows,       // 한 페이지 경과 수
            @Query("pageNo") int page_no,              // 페이지 번호
            @Query("dataType") String data_type,       // 응답 자료 형식
            @Query("base_date") String base_date,      // 발표 일자
            @Query("base_time") String base_time,      // 발표 시각
            @Query("nx") String nx,                    // 예보지점 X 좌표
            @Query("ny") String ny                     // 예보지점 Y 좌표
    );
}

// xml 파일 형식을 class로 구현
class WEATHER {
    public RESPONSE response;
}

class RESPONSE {
    public HEADER header;
    public BODY body;
}

class HEADER {
    public int resultCode;
    public String resultMsg;
}

class BODY {
    public String dataType;
    public ITEMS items;
    public int totalCount;
}

class ITEMS {
    public List<ITEM> item;
}

class ITEM {
    // category : 자료 구분 코드, fcstDate : 예측 날짜, fcstTime : 예측 시간, fcstValue : 예보 값
    public String category, fcstDate, fcstTime, fcstValue;
}

// retrofit을 사용하기 위한 빌더 생성
class RetrofitClient {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static WeatherInterface getRetrofitService() {
        return retrofit.create(WeatherInterface.class);
    }
}

