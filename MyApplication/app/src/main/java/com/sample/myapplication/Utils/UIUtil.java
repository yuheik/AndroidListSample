package com.sample.myapplication.Utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class UIUtil {
    private static InputMethodManager getInputMethodManager(View view) {
        return (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static void showKeyboard(View view) {
        getInputMethodManager(view).showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyboard(View view) {
        getInputMethodManager(view).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
