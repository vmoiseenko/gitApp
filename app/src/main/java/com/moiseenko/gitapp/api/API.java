package com.moiseenko.gitapp.api;

import com.moiseenko.gitapp.json.Commit;
import com.moiseenko.gitapp.json.Repositories;
import com.moiseenko.gitapp.utils.Constants;

import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;


public class API {

    public static void sendReposRequest(String mUsername, RequestInterceptor requestInterceptor, Callback<List<Repositories.Repos>> callback) {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.API_URL)
                .setLogLevel(Constants.RETROFIT_LOG_LEVEL)
                .setRequestInterceptor(requestInterceptor)
                .build();

        IUserRepos userRepos = restAdapter.create(IUserRepos.class);
        userRepos.getRepos( mUsername, callback);
    }

    public static void commitsRequest(String username, String repositoryName, Callback<List<Commit>> callback){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.API_URL)
                .setLogLevel(Constants.RETROFIT_LOG_LEVEL)
                .build();

        ICommits iCommits = restAdapter.create(ICommits.class);
        iCommits.getCommits(username, repositoryName, callback);

    }

}