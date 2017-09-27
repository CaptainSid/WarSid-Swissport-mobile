package com.swissport.www.swissport;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by USER-hp on 9/13/2017.
 */
public class SharedPreferencesManager   {

    public void saveSharedPreferences(String name, String value, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public void deleteSharedPreference (Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        settings.edit().remove(key).commit();
//        context.getSharedPreferences("CREDENTIALS", 0).edit().clear().commit();
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();

    }
}
