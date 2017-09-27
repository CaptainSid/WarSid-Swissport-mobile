package com.swissport.www.swissport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER-hp on 9/13/2017.
 */
public class ChangePassActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String MDP_URL = "http://192.168.1.10:3000/api/mobileChangerMDP";
//    public static final String MDP_URL = "http://10.198.0.199:3000/api/mobileChangerMDP";
    public static final String KEY_Pass = "motDePasse";
    public static final String KEY_NewPass = "nvMotDePasse";
    public static final String KEY_Mail = "email";

    private String motDePasse;
    private String nvMotDePasse;
    private String email;

    private TextView textViewFullName;
    private TextView textViewMatricule;
    private EditText editTextpassword1;
    private EditText editTextpassword2;
    private EditText editTextpassword3;
    private Button buttonChangePassword;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changermdp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textViewFullName = (TextView) findViewById(R.id.textViewFullName);
        textViewMatricule = (TextView) findViewById(R.id.textViewMatricule);
        editTextpassword1=(EditText) findViewById(R.id.editTextpassword1);
        editTextpassword2=(EditText) findViewById(R.id.editTextpassword2);
        editTextpassword3=(EditText) findViewById(R.id.editTextpassword3);
        buttonChangePassword=(Button) findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(this);
        chargerPage();
    }

    @Override
    public void onClick(View v) {
        if ( editTextpassword1.getText().toString().trim().isEmpty() || editTextpassword2.getText().toString().trim().isEmpty() || editTextpassword3.getText().toString().trim().isEmpty() )
        {

            Toast.makeText(ChangePassActivity.this,"Veuillez remplir les champs nécessaire", Toast.LENGTH_LONG).show();

        }else {
            Log.e("tag",editTextpassword2.getText().toString());
            Log.e("tag2",editTextpassword3.getText().toString());

            if ( editTextpassword2.getText().toString().equals(editTextpassword3.getText().toString()))
            {
                changerMotdePasse();
            }else {
                Toast.makeText(ChangePassActivity.this,"Erreur dans la confirmation du mot de passe,veuillez ecrire le mot de passe encore une fois", Toast.LENGTH_LONG).show();

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.changermdp_menu, menu);
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
    private  void chargerPage(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user = preferences.getString("user", "");
        try {
            JSONObject userJSON = new JSONObject(user);
            textViewFullName.setText(userJSON.getString("nom")+" "+userJSON.getString("prenom"));
            textViewMatricule.setText("-"+userJSON.getString("matricule")+"-");
            email=userJSON.getString("email");

        }catch (JSONException e)
        {
            Log.e("Json",e.toString());
        }

    }

    private void changerMotdePasse(){
        motDePasse = editTextpassword1.getText().toString().trim();
        nvMotDePasse = editTextpassword2.getText().toString().trim();
        StringRequest POSTRequest = new StringRequest(Request.Method.PUT, MDP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resJSON = new JSONObject(response);
                            String message = resJSON.getString("message");
                            Toast.makeText(ChangePassActivity.this,message, Toast.LENGTH_LONG).show();
                        }
                        catch (JSONException e) {
                            Log.e("Main json exc",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangePassActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("Main volley",error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(KEY_Pass, motDePasse);
                map.put(KEY_NewPass, nvMotDePasse);
                map.put(KEY_Mail, email);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(POSTRequest);

    }

}
