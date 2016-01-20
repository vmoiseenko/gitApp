package com.moiseenko.gitapp.fragments;

import android.app.Fragment;
import android.content.Context;

import com.moiseenko.gitapp.listeners.FragmentTransactionListener;

/**
 * Created by Viktar_Maiseyenka on 19.01.2016.
 */
public abstract class BaseFragment extends Fragment {

    private FragmentTransactionListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (FragmentTransactionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.setFragmentTitle(this.getTitle());
    }

    protected abstract String getTitle();

}
