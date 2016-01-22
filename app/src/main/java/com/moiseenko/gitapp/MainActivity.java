package com.moiseenko.gitapp;

import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.moiseenko.gitapp.database.OrmLiteHelper;
import com.moiseenko.gitapp.fragments.LoginFragment;
import com.moiseenko.gitapp.listeners.FragmentTransactionListener;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

import java.net.ConnectException;
import java.net.SocketTimeoutException;


/**
 * A login screen that offers login via username/password.
 */
public class MainActivity extends AppCompatActivity implements FragmentTransactionListener {

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OrmLiteHelper.setHelper(getApplicationContext());

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(R.id.container, LoginFragment.newInstance()).commit();

    }



//    private void populateAutoComplete() {
//        getLoaderManager().initLoader(0, null, this);
//    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */


//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        return new CursorLoader(this,
//                // Retrieve data rows for the device user's 'profile' contact.
//                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
//                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
//
//                // Select only username addresses.
//                ContactsContract.Contacts.Data.MIMETYPE +
//                        " = ?", new String[]{ContactsContract.CommonDataKinds.username
//                .CONTENT_ITEM_TYPE},
//
//                // Show primary username addresses first. Note that there won't be
//                // a primary username address if the user hasn't specified one.
//                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
//        List<String> usernames = new ArrayList<>();
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            usernames.add(cursor.getString(ProfileQuery.ADDRESS));
//            cursor.moveToNext();
//        }
//
//        addusernamesToAutoComplete(usernames);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> cursorLoader) {
//
//    }

//    private void addusernamesToAutoComplete(List<String> usernameAddressCollection) {
//        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(LoginActivity.this,
//                        android.R.layout.simple_dropdown_item_1line, usernameAddressCollection);
//
//        usernameField.setAdapter(adapter);
//    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void setFragmentTitle(String title) {
//        getSupportActionBar().getCustomView()
//        getSupportActionBar().setTitle(title);
        toolbar.setTitle(title);
//        toolbar.fi
    }

    public class RetrofitErrorHandler implements ErrorHandler {

        @Override
        public Throwable handleError(RetrofitError cause) {

            if (cause.isNetworkError()) {
                if (cause.getMessage().contains("authentication")) {
                    //401 errors
                    return new Exception("Invalid credentials. Please verify login info.");
                } else if (cause.getCause() instanceof SocketTimeoutException) {
                    //Socket Timeout
                    return new SocketTimeoutException("Connection Timeout. " +
                            "Please verify your internet connection.");
                } else {
                    //No Connection
                    return new ConnectException("No Connection. " +
                            "Please verify your internet connection.");
                }
            } else {
                return new Exception("Invalid credentials. Please verify login info.");
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d("Test", "permission was granted, yay! Do the contacts-related task you need to do.");

                } else {
                    Log.d("Test", "permission denied, boo! Disable the functionality that depends on this permission..");

                }
                return;
            }

        }
    }


}

