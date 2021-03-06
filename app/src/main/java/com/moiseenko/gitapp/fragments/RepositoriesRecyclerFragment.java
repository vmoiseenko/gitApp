package com.moiseenko.gitapp.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moiseenko.gitapp.MainActivity;
import com.moiseenko.gitapp.R;
import com.moiseenko.gitapp.adapters.RepositoryItemAdapter;
import com.moiseenko.gitapp.database.OrmLiteHelper;
import com.moiseenko.gitapp.json.Repositories;

import java.util.List;

/**
 * Created by vmoiseenko on 23.10.2015.
 */
public class RepositoriesRecyclerFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private RepositoryItemAdapter adapter;

    private static String REPOS_LIST = "repos_List";
    private Repositories repos;
    private List<Repositories.Repos> reposList;
    private View view;


    public RepositoriesRecyclerFragment() {
    }

    public static RepositoriesRecyclerFragment newInstance(Repositories repos) {

        Bundle args = new Bundle();
        args.putSerializable(REPOS_LIST, repos);

        RepositoriesRecyclerFragment fragment = new RepositoriesRecyclerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        repos = (Repositories) getArguments().getSerializable(REPOS_LIST);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Test", "RepositoriesRecyclerFragment.class onStart");
    }

    //    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.exit_menu, menu);
//    }



    //    public static RepositoriesRecyclerFragment newInstance(List<Repositories.Repos> repos) {
//        return new RepositoriesRecyclerFragment();
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(view != null){
            return view;
        }

        view = inflater.inflate(R.layout.fragment_recycler, container, false);
        reposList = repos.getReposes();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView = initRecyclerView(recyclerView);

        RepositoryItemAdapter.CardItemClickListener listener = new RepositoryItemAdapter.CardItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

//                showRepositoryInfo(view, position);
                showCollapsingFragment(view, position);
            }
        };

        adapter = new RepositoryItemAdapter(reposList, getActivity().getApplicationContext(), listener);
        OrmLiteHelper.getHelper().addListOfRepositories(reposList);

        recyclerView.setAdapter(adapter);


        return view;
    }

    private void showRepositoryInfo(View view, int position) {
        FullInfoFragment fullInfoFragment = FullInfoFragment.newInstance(reposList.get(position));
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack("fullInfoFragment");
        transaction.replace(R.id.container, fullInfoFragment);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){


            View repName = view.findViewById(R.id.tvRepName);
            fullInfoFragment.setSharedElementEnterTransition(
                    TransitionInflater.from(getActivity()).inflateTransition(R.transition.trans_move));
            fullInfoFragment.setEnterTransition(
//                        TransitionInflater.from(getActivity()).inflateTransition(R.transition.trans_move));
                    TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));

            fullInfoFragment.setRepNameId(repName.getTransitionName());
            transaction.addSharedElement(repName, repName.getTransitionName());

            setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.trans_move));
            setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
        }

        transaction.commit();
    }

    private void showCollapsingFragment(View view, int position) {
        ScrollingFragment scrollingFragment = ScrollingFragment.newInstance(reposList.get(position));
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack("scrollingFragment");
        transaction.replace(R.id.root_container, scrollingFragment);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            View repName = view.findViewById(R.id.tvRepName);
            scrollingFragment.setSharedElementEnterTransition(
                    TransitionInflater.from(getActivity()).inflateTransition(R.transition.trans_move));
            scrollingFragment.setEnterTransition(
//                        TransitionInflater.from(getActivity()).inflateTransition(R.transition.trans_move));
                    TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));

            scrollingFragment.setRepNameId(repName.getTransitionName());
            transaction.addSharedElement(repName, repName.getTransitionName());

            setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.trans_move));
            setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
        }

        transaction.commit();
    }

    private RecyclerView initRecyclerView(RecyclerView recyclerView) {
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.ACTION_STATE_SWIPE;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);

            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder fromPosition, RecyclerView.ViewHolder toPosition) {
                adapter.swapViews(fromPosition.getAdapterPosition(), toPosition.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return recyclerView;
    }

//    @Override
//    public boolean isDrawerEnabled() {
//        return true;
//    }

//    @Override
//    public String getTitle() {
//        return getString(R.string.drawer_my_cards);
//    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected String getTitle() {
        return getString(R.string.listOfRepositories);
    }
}
