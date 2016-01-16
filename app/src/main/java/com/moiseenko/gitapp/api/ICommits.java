package com.moiseenko.gitapp.api;

import com.moiseenko.gitapp.json.Commit;
import com.squareup.okhttp.Response;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Viktar_Maiseyenka on 16.01.2016.
 */
public interface ICommits {
    @GET("/repos/{owner}/{repos_name}/commits")
    void getCommits(@Path("owner") String owner, @Path("repos_name") String repositoryName, Callback<List<Commit>> callback);
}
