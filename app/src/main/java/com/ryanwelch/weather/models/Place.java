package com.ryanwelch.weather.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class Place implements SearchSuggestion {

    @SerializedName("lat")
    private double mLatitude;
    @SerializedName("lon")
    private double mLongitude;

    @SerializedName("name")
    private String mName;
    @SerializedName("region")
    private String mRegion;
    @SerializedName("country")
    private String mCountry;

    @SerializedName("id")
    private int mId;

    private boolean mIsHistory = false;

    public Place(String name, String region, String country) {
        this.mName = name;
        this.mRegion = region;
        this.mCountry = country;
    }

    public Place(Parcel source) {
        //this.mName = source.readString();
        //this.mIsHistory = source.readInt() != 0;
    }

    @Override
    public String getBody() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //parcel.writeString(mName);
        //parcel.writeInt(mIsHistory ? 1 : 0);
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = mLongitude;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getRegion() {
        return mRegion;
    }

    public void setRegion(String region) {
        mRegion = region;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
