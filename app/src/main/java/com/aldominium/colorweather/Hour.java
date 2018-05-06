package com.aldominium.colorweather;

import android.os.Parcel;
import android.os.Parcelable;

public class Hour implements Parcelable{

    private String title;
    private String weatherDescription;


    protected Hour(Parcel in) {
        title = in.readString();
        weatherDescription = in.readString();
    }

    public static final Creator<Hour> CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel in) {
            return new Hour(in);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };

    public Hour() { }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    @Override
    public String toString() {
        return "Hour{" +
                "title='" + title + '\'' +
                ", weatherDescription='" + weatherDescription + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(weatherDescription);
    }
}
