package com.example.rescuesupport;

public class DisasterMessage {
    private final String create_date;
    private final String location_name;
    private final String msg;

    public DisasterMessage(String create_date, String location_name, String msg) {
        this.create_date = create_date;
        this.location_name = location_name;
        this.msg = msg;
    }

    public String getCreate_date() {
        return create_date;
    }

    public String getLocation_name() {
        return location_name;
    }

    public String getMsg() {
        return msg;
    }
}

