package com.moiseenko.gitapp.fragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moiseenko.gitapp.MainActivity;
import com.moiseenko.gitapp.R;
import com.moiseenko.gitapp.adapters.CustomPagerAdapter;
import com.moiseenko.gitapp.json.Repositories;

import java.lang.reflect.Field;

/**
 * Created by Viktar_Maiseyenka on 21.01.2016.
 */
public class ScrollingFragment extends BaseFragment {

    private static String REPOS = "repos";
    private Repositories.Repos repository;
    private String repNameId;
    int notifyCount = 0;

    private FloatingActionButton fabNotification;


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

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        fabNotification = (FloatingActionButton) view.findViewById(R.id.fab);

        collapsingToolbarLayout.setTitle(repository.getName());
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new CustomPagerAdapter(getActivity()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            collapsingToolbarLayout.setTransitionName(repNameId);
        }


        fabNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        return view;
    }

    private void sendNotification() {
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(getActivity());
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);

        notificationCompat.setSmallIcon(android.R.drawable.ic_dialog_email);
        notificationCompat.setContentTitle("Scrolling Notification");
        notificationCompat.setContentText(repository.getFull_name());
        notificationCompat.setNumber(++notifyCount);
        notificationCompat.setContentIntent(contentIntent);
        notificationCompat.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationCompat.build());


    }

    @Override
    protected String getTitle() {
        return getString(R.string.listOfRepositories);
    }

    public void setRepNameId(String repNameId) {
        this.repNameId = repNameId;
    }

}
