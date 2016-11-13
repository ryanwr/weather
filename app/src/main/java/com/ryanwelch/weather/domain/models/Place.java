package com.ryanwelch.weather.domain.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;
import com.ryanwelch.weather.data.db.PlaceTable;

@StorIOSQLiteType(table = PlaceTable.TABLE)
public class Place implements SearchSuggestion {

    @StorIOSQLiteColumn(name = PlaceTable.COLUMN_ID, key = true)
    Double id;

    @SerializedName("lat")
    @StorIOSQLiteColumn(name = PlaceTable.COLUMN_LATITUDE)
    Double latitude;

    @SerializedName("lon")
    @StorIOSQLiteColumn(name = PlaceTable.COLUMN_LONGITUDE)
    Double longitude;

    @SerializedName("name")
    @StorIOSQLiteColumn(name = PlaceTable.COLUMN_NAME)
    String name;

    @SerializedName("region")
    @StorIOSQLiteColumn(name = PlaceTable.COLUMN_REGION)
    String region;

    @SerializedName("country")
    @StorIOSQLiteColumn(name = PlaceTable.COLUMN_COUNTRY)
    String country;

    @StorIOSQLiteColumn(name = PlaceTable.COLUMN_DISPLAY_ORDER)
    Integer displayOrder = 0;

    Place() {}

    public Place(String name, String region, String country, double latitude, double longitude) {
        this.name = name;
        this.region = region;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Place(Parcel source) {
        this.latitude = source.readDouble();
        this.longitude = source.readDouble();
        this.name = source.readString();
        this.region = source.readString();
        this.country = source.readString();
        this.displayOrder = source.readInt();
    }

    @Override
    public String getBody() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(name);
        parcel.writeString(region);
        parcel.writeString(country);
        parcel.writeInt(displayOrder);
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
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
