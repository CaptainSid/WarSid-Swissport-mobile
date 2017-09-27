package com.swissport.www.swissport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by USER-hp on 9/20/2017.
 */
public class RequestActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextRequest;
    private Button buttonSend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editTextRequest = (EditText) findViewById(R.id.editTextMessage);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.request_menu, menu);
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
            case R.id.action_notification:
                route.goToNotifications(getBaseContext());
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
    @Override
    public void onClick(View v) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.contains("user")) {
            String user = preferences.getString("user", "");
            try {
                JSONObject userJSON = new JSONObject(user);
                String name = userJSON.getString("nom");
                String fullname ="("+ name + " " + userJSON.getString("prenom") + ") ";
                String message = editTextRequest.getText().toString().trim();
                if (SocketManager.mSocket != null)
                {
                    SocketManager.mSocket.emit("newMessage", fullname + message);
                    Toast.makeText(RequestActivity.this,"Votre message a bien été envoyé", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(RequestActivity.this,"Votre message n'a pas été envoyé, veuillez vérifier votre connextion", Toast.LENGTH_LONG).show();

                }


            } catch (JSONException e) {
                Log.e("Main json ect", e.toString());
            }

        }
    }
}