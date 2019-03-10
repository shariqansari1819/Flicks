package com.codebosses.flicks.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

public class ValidUtils {

    static String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    static String emailpattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
    public static String terminal_id;

    public static boolean validateEditTexts(EditText... testObj) {
        for (int i = 0; i < testObj.length; i++) {
            if (testObj[i].getText().toString().trim().equals(""))
                return false;
        }
        return true;
    }

    public static boolean validateMobileNumber(EditText... testObj) {
        for (int i = 0; i < testObj.length; i++) {
            if (testObj[i].getText().toString().length() != 10)
                return false;
        }
        return true;
    }


    public static boolean validateEmail(EditText... testObj) {
        for (int i = 0; i < testObj.length; i++) {
            if (!testObj[i].getText().toString().trim().matches(emailpattern) && !testObj[i].getText().toString().trim().matches(emailpattern2))
                return false;
        }
        return true;
    }

    public static boolean validateForDigits(EditText testObj, int noOfDigits) {
        if (testObj.getText().toString().length() != noOfDigits)
            return false;
        return true;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

    public static boolean validatePassword(String password) {
        if (password.length() <= 6)
            return false;
        return true;
    }

    public static void hideKeyboardFromActivity(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboardFromFragment(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void changeToolbarFont(Toolbar toolbar, Activity context) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(toolbar.getTitle())) {
                    FontUtils.getFontUtils(context).setTextViewRegularFont(tv);
                    break;
                }
            }
        }
    }
}
