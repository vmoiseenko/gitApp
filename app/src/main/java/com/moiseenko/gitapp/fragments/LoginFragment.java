package com.moiseenko.gitapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.moiseenko.gitapp.R;
import com.moiseenko.gitapp.api.IAuth;
import com.moiseenko.gitapp.api.IUserRepos;
import com.moiseenko.gitapp.json.Repositories;
import com.moiseenko.gitapp.utils.Constants;
import com.moiseenko.gitapp.utils.Utils;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Виктор on 11.01.2016.
 */
public class LoginFragment extends Fragment {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private TextInputLayout usernameView;
    private EditText usernameField;
    private EditText mPasswordView;
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

        usernameView = (TextInputLayout) view.findViewById(R.id.username_view);
//        usernameView.setErrorEnabled(false);

//        usernameView.setError(Html.fromHtml("<font color=\"#A9A9A9\">" + "Введите имя пользователя" + "</font>"));
        usernameField = (EditText) view.findViewById(R.id.username);


        mPasswordView = (EditText) view.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
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
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = usernameField.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
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
                    .build();

            //            loginRequest(restAdapter);
            reposRequest(restAdapter);
            //            try {
            //                Thread.sleep(1500);
            //            } catch (InterruptedException e) {
            //                return false;
            //            }


            return true;
        }

        private void reposRequest(RestAdapter restAdapter) {
            IUserRepos userRepos = restAdapter.create(IUserRepos.class);
            userRepos.getRepos(mUsername, new Callback<List<Repositories.Repos>>() {


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
                    showProgress(false);
                    Log.d("TEST", error.getLocalizedMessage());
                    usernameView.setError(error.getLocalizedMessage());
                }
            });
        }

        private void loginRequest(RestAdapter restAdapter) {
            IAuth iAuth = restAdapter.create(IAuth.class);
            try {
                Log.d("TEST", mUsername + ":" + mPassword);
                Log.d("TEST", Utils.getBase64String(mUsername + ":" + mPassword));
                Log.d("TEST", Utils.getBase64String(mUsername));

                iAuth.login(Utils.getBase64String(mUsername), new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        Log.d("TEST", "retrofitOk");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("TEST", error.getLocalizedMessage());
                        usernameView.setError(error.getLocalizedMessage());
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
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
