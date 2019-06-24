package com.kitesurfing.diana.internshipab4app.templates.SpotsList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpotsListBody {

    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("windProbability")
    @Expose
    private int windProbability;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getWindProbability() {
        return windProbability;
    }

    public void setWindProbability(int windProbability) {
        this.windProbability = windProbability;
    }

}