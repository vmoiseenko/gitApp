package com.moiseenko.gitapp.api;

import retrofit.http.GET;

public interface API {
    @GET("/teams")
    AuthObject getUsers();
}