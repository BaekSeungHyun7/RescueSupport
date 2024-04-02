package com.example.rescuesupport;

import com.google.gson.annotations.SerializedName;

public class WeatherMainModel {

    @SerializedName("temp")
    double temp = 0.0;  //현재온도

    @SerializedName("humidity")
    double humidity = 0.0;  //현재습도

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

}
