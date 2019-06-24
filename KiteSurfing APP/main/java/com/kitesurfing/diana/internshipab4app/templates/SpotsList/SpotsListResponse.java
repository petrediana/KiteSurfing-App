package com.kitesurfing.diana.internshipab4app.templates.SpotsList;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpotsListResponse {

    @SerializedName("result")
    @Expose
    private List<Spot> result = null;

    public List<Spot> getResult() {
        return result;
    }

    public void setResult(List<Spot> result) {
        this.result = result;
    }

}