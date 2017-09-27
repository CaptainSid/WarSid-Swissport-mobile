package com.swissport.www.swissport;

import android.app.Application;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by USER-hp on 9/11/2017.
 */
public class LaunchApp extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
//        SharedPreferences sharedPrefs = getSharedPreference()
//        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

/*
        if (mSharedPreference.contains("logged"))
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
*/

    }


}
