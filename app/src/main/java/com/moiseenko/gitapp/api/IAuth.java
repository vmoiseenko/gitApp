package com.moiseenko.gitapp.api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by Виктор on 10.01.2016.
 */
public interface IAuth {
//    @FormUrlEncoded
    @GET("/user") void login(
            @Header("Authorization") String authorization,
            Callback<Response> callback
    );

}
