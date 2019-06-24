package com.kitesurfing.diana.internshipab4app.templates.FavoriteSpot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavoriteSpotBody {

    @SerializedName("spotId")
    @Expose
    private String spotId;

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

}