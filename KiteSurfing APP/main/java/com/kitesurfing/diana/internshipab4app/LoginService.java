package com.kitesurfing.diana.internshipab4app;

import com.kitesurfing.diana.internshipab4app.templates.Login.LoginBody;
import com.kitesurfing.diana.internshipab4app.templates.Login.LoginToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("api-user-get")
    Call<LoginToken> getLoginToken(@Body LoginBody loginBody);
}
