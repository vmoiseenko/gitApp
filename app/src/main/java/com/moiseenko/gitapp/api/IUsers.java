package com.moiseenko.gitapp.api;

import com.squareup.okhttp.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public interface IUsers {
    @GET("/users")
    Response getUsers(@Query("username") String username);
}
