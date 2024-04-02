package com.example.rescuesupport;

// 날씨 정보를 담는 클래스
public class ModelWeather {

    public String rainType = "";        // 강수 형태
    public String humidity = "";       // 습도
    public String sky = "";            // 하늘 상태
    public String temp = "";           // 기온
    public String fcstTime = "";       // 예보시각

    // 기본 생성자
    public ModelWeather() {}

    // getter 메서드들
    public String getRainType() {
        return rainType;
    }


    public String getHumidity() {
        return humidity;
    }

    public String getSky() {
        return sky;
    }

    public String getTemp() {
        return temp;
    }

    public String getFcstTime() {
        return fcstTime;
    }

    // setter 메서드들
    public void setRainType(String rainType) {
        this.rainType = rainType;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setSky(String sky) {
        this.sky = sky;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setFcstTime(String fcstTime) {
        this.fcstTime = fcstTime;
    }
}

