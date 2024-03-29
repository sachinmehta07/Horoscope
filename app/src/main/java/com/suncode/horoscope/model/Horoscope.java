package com.suncode.horoscope.model;

import com.google.gson.annotations.SerializedName;

public class Horoscope {
    @SerializedName("date_range")
    private String dateRange;

    @SerializedName("current_date")
    private String currentDate;

    private String description;
    private String compatibility;
    private String mood;
    private String color;

    @SerializedName("lucky_number")
    private String luckyNumber;

    @SerializedName("lucky_time")
    private String luckyTime;

    public String getDateRange() {
        return dateRange;
    }

    public Horoscope(String currentDate, String compatibility, String luckyTime, String luckyNumber, String color, String dateRange, String mood, String description) {
        this.dateRange = dateRange;
        this.currentDate = currentDate;
        this.description = description;
        this.compatibility = compatibility;
        this.mood = mood;
        this.color = color;
        this.luckyNumber = luckyNumber;
        this.luckyTime = luckyTime;
    }
    public String getCurrentDate() {
        return currentDate;
    }

    public String getDescription() {
        return description;
    }

    public String getCompatibility() {
        return compatibility;
    }

    public String getMood() {
        return mood;
    }

    public String getColor() {
        return color;
    }

    public String getLuckyNumber() {
        return luckyNumber;
    }

    public String getLuckyTime() {
        return luckyTime;
    }
}
