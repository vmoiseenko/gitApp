package com.moiseenko.gitapp.fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.moiseenko.gitapp.api.IAuth;
import com.moiseenko.gitapp.api.IUserRepos;
import com.moiseenko.gitapp.json.Error;
import com.moiseenko.gitapp.json.Repositories;
import com.moiseenko.gitapp.utils.Constants;
import com.moiseenko.gitapp.utils.Utils;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Виктор on 11.01.2016.
 */
public class LoginFragment extends BaseFragment {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private TextInputLayout usernameView;
    private EditText usernameField;
    private EditText mPasswordField;
    private SwitchCompat mAuthSwitch;
    private View mProgressView;
    private View mLoginFormView;
    private View mPasswordFormView;

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


        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setMessage("We want to know your Contacts, so you will need to give access for us! ;)")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.READ_CONTACTS},
                                        MainActivity.MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MainActivity.MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        }

        usernameView = (TextInputLayout) view.findViewById(R.id.username_view);
        usernameField = (EditText) view.findViewById(R.id.username);


        mPasswordFormView = view.findViewById(R.id.passwordView);
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
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        usernameView.setError(null);
        mPasswordField.setError(null);

        // Store values at the time of the login attempt.
        String username = usernameField.getText().toString();
        String password = mPasswordField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordField.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordField;
            cancel = true;
        }

        // Check for a valid username address.
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
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected String getTitle() {
        return "Login";
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.API_URL)//В метод setEndpoint передаем адрес нашего сайта
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            if(mAuthSwitch.isChecked()){
                                try {
                                    request.addHeader("Authorization", "Basic "+Utils.getBase64String(mUsername + ":" + mPassword));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    })
                    .build();

//                loginRequest(restAdapter);
            reposRequest(restAdapter);


            return true;
        }

        private void reposRequest(RestAdapter restAdapter) {
            IUserRepos userRepos = restAdapter.create(IUserRepos.class);
            userRepos.getRepos( mUsername, new Callback<List<Repositories.Repos>>() {
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
                                    .addToBackStack("LoginFragment")
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

        private void loginRequest(RestAdapter restAdapter) {
            IAuth iAuth = restAdapter.create(IAuth.class);
            try {
//                Log.d("TEST", mUsername + ":" + mPassword);
//                Log.d("TEST", Utils.getBase64String(mUsername + ":" + mPassword));
//                Log.d("TEST", Utils.getBase64String(mUsername));

                iAuth.login("Basic "+Utils.getBase64String(mUsername + ":" + mPassword), new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        showProgress(false);

                        Log.d("TEST", "retrofitOk");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("TEST", error.getLocalizedMessage());
                        Error e = (Error) error.getBodyAs(Error.class);
                        usernameView.setError(e.getMessage());
                        showProgress(false);
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (!success) {
                showProgress(false);
                mPasswordField.setError(getString(R.string.error_incorrect_password));
                mPasswordField.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
