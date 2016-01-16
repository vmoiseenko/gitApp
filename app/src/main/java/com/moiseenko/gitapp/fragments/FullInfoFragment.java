package com.moiseenko.gitapp.fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.moiseenko.gitapp.R;
import com.moiseenko.gitapp.json.Repositories;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Виктор on 11.01.2016.
 */
public class FullInfoFragment extends Fragment {

    private String repNameId;
    private Repositories.Repos repos;
    private static String REPOS = "repos";


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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvName.setTransitionName(repNameId);
        }
        tvName.setText(repos.getName());
        tvFullName.setText(repos.getFull_name());

        return rootView;
    }

    public void setRepNameId(String repNameId) {
        this.repNameId = repNameId;
    }
}
