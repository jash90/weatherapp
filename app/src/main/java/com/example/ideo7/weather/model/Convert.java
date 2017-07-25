package com.example.ideo7.weather.model;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class Convert {

    public static String getlang() {

        String[] lang = new String[]{"ar", "bg", "ca", "cz", "de", "el", "en", "fa", "fi", "fr", "gl", "hr", "hu", "it",
                "ja", "kr", "la", "lt", "mk", "nl", "pl", "pt", "ro", "ru", "se", "sk", "sl", "es", "tr", "ua", "vi",
                "zh_cn", "zh_tw"};
        List<String> langs = Arrays.asList(lang);
        return langs.contains(Locale.getDefault().getLanguage()) ? Locale.getDefault().getLanguage() : "en";
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    0);
        }
    }

}
