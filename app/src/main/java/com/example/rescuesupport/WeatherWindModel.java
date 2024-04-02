package com.example.rescuesupport;

import com.google.gson.annotations.SerializedName;

public class WeatherWindModel {

    @SerializedName("speed")
    double speed = 0.0;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

}