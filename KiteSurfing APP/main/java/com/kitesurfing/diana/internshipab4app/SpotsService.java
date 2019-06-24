package com.kitesurfing.diana.internshipab4app;

import com.kitesurfing.diana.internshipab4app.templates.FavoriteSpot.FavoriteSpotBody;
import com.kitesurfing.diana.internshipab4app.templates.FavoriteSpot.FavoriteSpotResponse;
import com.kitesurfing.diana.internshipab4app.templates.SpotDetails.SpotDetailsBody;
import com.kitesurfing.diana.internshipab4app.templates.SpotDetails.SpotDetailsResponse;
import com.kitesurfing.diana.internshipab4app.templates.SpotsCountries.SpotsCountriesResponse;
import com.kitesurfing.diana.internshipab4app.templates.SpotsList.SpotsListBody;
import com.kitesurfing.diana.internshipab4app.templates.SpotsList.SpotsListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SpotsService {

    @POST("api-spot-get-all")
    Call<SpotsListResponse> getSpotsList(@Header("token") String token, @Body SpotsListBody spotsListBody);

    @POST("api-spot-get-details")
    Call<SpotDetailsResponse> getSpotDetails(@Header("token") String token, @Body SpotDetailsBody spotDetailsBody);

    @POST("api-spot-favorites-add")
    Call<FavoriteSpotResponse> addSpotToFavorites(@Header("token") String token, @Body FavoriteSpotBody favoriteSpotBody);

    @POST("api-spot-favorites-remove")
    Call<FavoriteSpotResponse> removeSpotFromFavorites(@Header("token") String token, @Body FavoriteSpotBody favoriteSpotBody);

    @POST("api-spot-get-countries")
    Call<SpotsCountriesResponse> getSpotsCountries(@Header("token") String token);
}
