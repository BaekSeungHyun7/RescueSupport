package com.example.rescuesupport;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherInfoModel {

    @SerializedName("name")
    String name = "";  //도시이름

    @SerializedName("weather")
    List<WeatherWeatherModel> weather;

    @SerializedName("main")
    WeatherMainModel main;

    @SerializedName("wind")
    WeatherWindModel wind;

    @SerializedName("sys")
    WeatherSysModel sys;

    @SerializedName("clouds")
    WeatherCloudsModel clouds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WeatherWeatherModel> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherWeatherModel> weather) {
        this.weather = weather;
    }

    public WeatherMainModel getMain() {
        return main;
    }

    public void setMain(WeatherMainModel main) {
        this.main = main;
    }

    public WeatherWindModel getWind() {
        return wind;
    }

    public void setWind(WeatherWindModel wind) {
        this.wind = wind;
    }

    public WeatherSysModel getSys() {
        return sys;
    }

    public void setSys(WeatherSysModel sys) {
        this.sys = sys;
    }

    public WeatherCloudsModel getClouds() {
        return clouds;
    }

    public void setClouds(WeatherCloudsModel clouds) {
        this.clouds = clouds;
    }

}
