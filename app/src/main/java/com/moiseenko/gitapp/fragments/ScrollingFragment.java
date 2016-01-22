package com.moiseenko.gitapp.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moiseenko.gitapp.R;
import com.moiseenko.gitapp.json.Repositories;

import java.lang.reflect.Field;

/**
 * Created by Viktar_Maiseyenka on 21.01.2016.
 */
public class ScrollingFragment extends BaseFragment {

    private static String REPOS = "repos";
    private Repositories.Repos repository;
    private String repNameId;


    public static ScrollingFragment newInstance(Repositories.Repos repos) {
        
        Bundle args = new Bundle();
        args.putSerializable(REPOS, repos);
        
        ScrollingFragment fragment = new ScrollingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!= null){
            repository = (Repositories.Repos) getArguments().getSerializable(REPOS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_scrolling, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());

        Field f = null;
        try {
            f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            TextView titleTextView = (TextView) f.get(toolbar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                titleTextView.setTransitionName(repNameId);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return view;
    }

    @Override
    protected String getTitle() {
        return repository.getName();
    }

    public void setRepNameId(String repNameId) {
        this.repNameId = repNameId;
    }

}
