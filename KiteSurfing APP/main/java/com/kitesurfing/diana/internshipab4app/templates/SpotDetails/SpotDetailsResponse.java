package com.kitesurfing.diana.internshipab4app.templates.SpotDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpotDetailsResponse {

    @SerializedName("result")
    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}