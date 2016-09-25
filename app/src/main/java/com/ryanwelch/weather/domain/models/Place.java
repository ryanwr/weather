package com.ryanwelch.weather.domain.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;
import com.ryanwelch.weather.data.db.PlaceTable;

@StorIOSQLiteType(table = PlaceTable.TABLE_PLACES)
public class Place implements SearchSuggestion {

    @StorIOSQLiteColumn(name = PlaceTable.COLUMN_ID, key = true)
    double mId;

    @SerializedName("lat")
    @StorIOSQLiteColumn(name = PlaceTable.COLUMN_LATITUDE)
    double mLatitude;

    @SerializedName("lon")
    @StorIOSQLiteColumn(name = PlaceTable.COLUMN_LONGITUDE)
    double mLongitude;

    @SerializedName("name")
    @StorIOSQLiteColumn(name = PlaceTable.COLUMN_NAME)
    String mName;

    @SerializedName("region")
    @StorIOSQLiteColumn(name = PlaceTable.COLUMN_REGION)
    String mRegion;

    @SerializedName("country")
    @StorIOSQLiteColumn(name = PlaceTable.COLUMN_COUNTRY)
    String mCountry;

    Place() {}

    public Place(String name, String region, String country, double latitude, double longitude) {
        this.mName = name;
        this.mRegion = region;
        this.mCountry = country;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public Place(Parcel source) {
        this.mLatitude = source.readDouble();
        this.mLongitude = source.readDouble();
        this.mName = source.readString();
        this.mRegion = source.readString();
        this.mCountry = source.readString();
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
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        parcel.writeString(mName);
        parcel.writeString(mRegion);
        parcel.writeString(mCountry);
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

    @Override
    public boolean equals(Object o) {
        if(o instanceof Place){
            Place other = (Place) o;
            return this.getLongitude() == other.getLongitude()
                    && this.getLatitude() == other.getLatitude();
        }
        return false;
    }
}
