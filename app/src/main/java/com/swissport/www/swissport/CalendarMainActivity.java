package com.swissport.www.swissport;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class CalendarMainActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.main_container, new com.swissport.www.swissport.MaterialCalendarFragment()).commit();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agenda_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle app bar item clicks here. The app bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        RoutesManager route=new RoutesManager();
        SharedPreferencesManager Manager=new SharedPreferencesManager();
        switch (item.getItemId()) {
            case R.id.action_compte:
                route.goToProfle(getBaseContext());
                return true;
            case R.id.action_notification:
                route.goToNotifications(getBaseContext());
                return true;
            case R.id.action_request:
                route.newRequest(getBaseContext());
                return true;
            case R.id.action_logout:
                Manager.deleteSharedPreference(getApplicationContext());
                route.goToLogin(getBaseContext());
                return true;

            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }
}

