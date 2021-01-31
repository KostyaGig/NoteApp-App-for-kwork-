package com.admin.notes.models;

public class Time {

    String hour;
    String minute;
    String currentTime;

    public Time() {
    }

    public Time(String hour, String minute, String currentTime) {
        this.hour = hour;
        this.minute = minute;
        this.currentTime = currentTime;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
