package com.aldominium.colorweather;

public class Hour {

    private String title;
    private String weatherDescription;


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
}
