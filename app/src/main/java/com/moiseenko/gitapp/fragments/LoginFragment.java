package com.moiseenko.gitapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.moiseenko.gitapp.MainActivity;
import com.moiseenko.gitapp.R;
import com.moiseenko.gitapp.api.API;
import com.moiseenko.gitapp.json.Error;
import com.moiseenko.gitapp.json.Repositories;
import com.moiseenko.gitapp.utils.Permissions;
import com.moiseenko.gitapp.utils.Utils;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Виктор on 11.01.2016.
 */
public class LoginFragment extends BaseFragment{

    private TextInputLayout usernameView;
    private TextInputLayout mPasswordFormView;
    private EditText usernameField;
    private EditText mPasswordField;
    private SwitchCompat mAuthSwitch;
    private View mProgressView;
    private View mLoginFormView;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Permissions.checkContactPermision(getActivity());

        usernameView = (TextInputLayout) view.findViewById(R.id.username_view);
        usernameField = (EditText) view.findViewById(R.id.username);

        mPasswordFormView = (TextInputLayout) view.findViewById(R.id.passwordView);
        mPasswordField = (EditText) view.findViewById(R.id.password);
        mPasswordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mAuthSwitch = (SwitchCompat) view.findViewById(R.id.authSwitch);
        mAuthSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuthSwitch.isChecked()){
                    mPasswordFormView.setVisibility(View.VISIBLE);
                } else {
                    mPasswordFormView.setVisibility(View.GONE);
                }
            }
        });


        Button bLogin = (Button) view.findViewById(R.id.bLogin);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(view, getActivity());
                attemptLogin();
            }
        });

        mLoginFormView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);

        return view;
    }

    private void attemptLogin() {

        // Reset errors.
        usernameView.setError(null);
        mPasswordFormView.setError(null);

        // Store values at the time of the login attempt.
        final String username = usernameField.getText().toString();
        final String password = mPasswordField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password) && mAuthSwitch.isChecked()) {
            mPasswordFormView.setError(getString(R.string.error_field_required));
            focusView = mPasswordField;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameField;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            usernameView.setError(getString(R.string.error_invalid_username));
            focusView = usernameField;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            loginRequest(username, password);
        }
    }

    private void loginRequest(final String username, final String password) {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                if(mAuthSwitch.isChecked()){
                    try {
                        request.addHeader("Authorization", "Basic "+ Utils.getBase64String(username + ":" + password));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        API.sendReposRequest(username, requestInterceptor, new Callback<List<Repositories.Repos>>() {
            @Override
            public void success(final List<Repositories.Repos> repositories, Response response) {
                Log.d("TEST", "retrofitOk");
                for (int i = 0; i < repositories.size(); i++) {
                    Log.d("TEST", "repository #" + (i + 1) + " " + repositories.get(i).getName());
                }
                showProgress(false);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getFragmentManager().beginTransaction()
//                                .addToBackStack("LoginFragment")
                                .replace(R.id.container, RepositoriesRecyclerFragment.newInstance(new Repositories(repositories)))
                                .commit();
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("TEST", error.getLocalizedMessage());
                Error e = (Error) error.getBodyAs(Error.class);
                usernameView.setError(e.getMessage());
                showProgress(false);
            }
        });
    }


    private boolean isUsernameValid(String username) {
        return username.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected String getTitle() {
        return "Login";
    }

}
