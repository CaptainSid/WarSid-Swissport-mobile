package com.swissport.www.swissport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by USER-hp on 9/27/2017.
 */
public class NotificationActivity extends AppCompatActivity {
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mListView = (ListView) findViewById(R.id.notification_list_view);
// 1
//        final ArrayList<String> recipeList=new
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if ( preferences.contains("user")){
            String user = preferences.getString("user", "");
            try{
                JSONObject userJSON = new JSONObject(user);
                JSONArray notifications=userJSON.getJSONArray("allNotifications");
                // 2
                String[] listItems = new String[notifications.length()];
// 3
                for(int i = 0; i < notifications.length(); i++){
                    JSONObject notif = (JSONObject)notifications.get(i);
                    listItems[i] = notif.getString("text");
                    Log.e("notification acitivity",listItems[i]);
                }
// 4
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
                mListView.setAdapter(adapter);
            }
            catch (JSONException e) {
                Log.e("Main json ect",e.toString());
            }
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notification_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle app bar item clicks here. The app bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        RoutesManager route=new RoutesManager();
        SharedPreferencesManager Manager=new SharedPreferencesManager();
        switch (item.getItemId()) {
            case R.id.action_agenda:
                route.goToCalendar(getBaseContext());
                return true;
            case R.id.action_request:
                route.newRequest(getBaseContext());
                return true;
            case R.id.action_compte:
                route.goToProfle(getBaseContext());
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
