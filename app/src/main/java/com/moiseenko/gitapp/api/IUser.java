package com.moiseenko.gitapp.api;

import com.squareup.okhttp.Response;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface IUser {
    @GET("/users/{username}")
    void getAuth(@Path("username") String username, Callback<Response> callback);
}
