package com.kitesurfing.diana.internshipab4app.templates.SpotsCountries;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpotsCountriesResponse {

    @SerializedName("result")
    @Expose
    private List<String> result = null;

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

}