package com.moiseenko.gitapp.api;

import com.moiseenko.gitapp.json.Repositories;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

import java.util.List;

public interface IUserRepos {
    @GET("/users/{username}/repos")
    void getRepos(@Path("username") String username, Callback<List<Repositories.Repos>> callback);
}
