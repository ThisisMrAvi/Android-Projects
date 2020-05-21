package com.mravi.earthquakedata;

public class Earthquake {

    private double mMagnitude;
    private String mLocation;
    private String mDate;
    private String mUrl;

    public Earthquake(double magnitude, String location, String date, String url) {
        mMagnitude = magnitude;
        mLocation = location;
        mDate = date;
        mUrl = url;
    }

    public String getMagnitude() {
        return String.valueOf(mMagnitude);
    }

    public String getDate() {
        return mDate;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getUrl() {
        return mUrl;
    }
}
