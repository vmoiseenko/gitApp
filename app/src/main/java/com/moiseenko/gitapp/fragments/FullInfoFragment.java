package com.moiseenko.gitapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.moiseenko.gitapp.MainActivity;
import com.moiseenko.gitapp.R;
import com.moiseenko.gitapp.api.API;
import com.moiseenko.gitapp.api.ICommits;
import com.moiseenko.gitapp.api.IUserRepos;
import com.moiseenko.gitapp.json.Commit;
import com.moiseenko.gitapp.json.Error;
import com.moiseenko.gitapp.json.Repositories;
import com.moiseenko.gitapp.utils.Constants;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Виктор on 11.01.2016.
 */
public class FullInfoFragment extends BaseFragment {

    private String repNameId;
    private Repositories.Repos repos;
    private static String REPOS = "repos";

    private TextView tvLastCommit;
    private ProgressBar progressBar;

    public FullInfoFragment() {
    }

    public static FullInfoFragment newInstance(Repositories.Repos repos) {

        Bundle args = new Bundle();
        args.putSerializable(REPOS, repos);

        FullInfoFragment fragment = new FullInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!= null){
            repos = (Repositories.Repos)getArguments().getSerializable(REPOS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_full_info, container, false);

        TextView tvName = (TextView) rootView.findViewById(R.id.tvRepName);
        TextView tvFullName = (TextView) rootView.findViewById(R.id.tvFullName);
        tvLastCommit = (TextView) rootView.findViewById(R.id.tvLastCommit);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvName.setTransitionName(repNameId);
        }
        tvName.setText(repos.getName());
        tvFullName.setText(repos.getFull_name());

        commitsRequest();

        return rootView;
    }

    public void setRepNameId(String repNameId) {
        this.repNameId = repNameId;
    }

    private void commitsRequest() {

        API.commitsRequest(repos.getOwner().getLogin(), repos.getName(), new Callback<List<Commit>>() {

            @Override
            public void success(List<Commit> commits, Response response) {
                Log.d("TEST", "retrofitOk");
                progressBar.setVisibility(View.GONE);
                for (int i = 0; i < commits.size(); i++) {
                    String message = commits.get(i).getCommit().getMessage();
                    Log.d("TEST", "commit #" + (i + 1) + " " + message);
                    tvLastCommit.append("\n" + message + "\n");
                }


            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("TEST", error.getLocalizedMessage());
                progressBar.setVisibility(View.GONE);
                Error e = (Error) error.getBodyAs(Error.class);
                tvLastCommit.append(e.getMessage());
            }
        });
    }

    @Override
    protected String getTitle() {
        return "Repository info";
    }
}
