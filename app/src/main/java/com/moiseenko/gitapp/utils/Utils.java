package com.moiseenko.gitapp.utils;


import android.content.Context;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;

/**
 * Created by vmoiseenko on 21.10.2015.
 */
public class Utils {

    public static void showKeyboard(final Context context, final EditText editText) {
        editText.post(new Runnable() {
            public void run() {
                editText.clearFocus();
                editText.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (android.os.Build.VERSION.SDK_INT < 11) {
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
                } else {
                    inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
                editText.setSelection(editText.getText().length());
            }
        });
    }

    public static void hideKeyboard(final View view, final Context context) {
        if (view == null) return;
        view.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    public static String getBase64String(String value) throws UnsupportedEncodingException {
        return Base64.encodeToString(value.getBytes("UTF-8"), Base64.NO_WRAP);
    }

    
}
