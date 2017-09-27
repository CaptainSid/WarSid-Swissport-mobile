package com.swissport.www.swissport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by USER-hp on 9/12/2017.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textViewFullName;
    private TextView textViewMatricule;
    private TextView textViewEmail;
    private TextView textViewDateDeNaissance;
    private TextView textViewNbHeures;
    private TextView textViewNbHeuresMois;
    private TextView textViewNbAbsences;
    private TextView textViewNbOff;
    private Button buttonChangePassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewFullName = (TextView) findViewById(R.id.textViewFullName);
        textViewMatricule = (TextView) findViewById(R.id.textViewMatricule);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewDateDeNaissance = (TextView) findViewById(R.id.textViewDateDeNaissance);
        textViewNbHeures = (TextView) findViewById(R.id.textViewNbHeures);
        textViewNbHeuresMois = (TextView) findViewById(R.id.textViewNbHeuresMois);
        textViewNbAbsences = (TextView) findViewById(R.id.textViewNbAbsences);
        textViewNbOff = (TextView) findViewById(R.id.textViewNbOff);
        buttonChangePassword = (Button) findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(this);
//        buttonPlanning=(ImageButton) findViewById(R.id.send_icon);
//        buttonPlanning.setOnClickListener(this);
        chargerCompte();
    }

    @Override
    public void onClick(View v) {
        RoutesManager route=new RoutesManager();
        SharedPreferencesManager Manager=new SharedPreferencesManager();
        switch (v.getId()){
            case R.id.buttonChangePassword:
                route.changePass(getBaseContext());
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profil_menu, menu);
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
            case R.id.action_logout:
                Manager.deleteSharedPreference(getApplicationContext());
                route.goToLogin(getBaseContext());
                return true;

            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    private  void chargerCompte(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user = preferences.getString("user" +
                "", "");
        try {
            JSONObject userJSON = new JSONObject(user);
           textViewFullName.setText("-"+userJSON.getString("nom")+" "+userJSON.getString("prenom")+"-");
           textViewMatricule.setText(userJSON.getString("matricule"));
           textViewEmail.setText("Email : "+userJSON.getString("email"));
           textViewDateDeNaissance.setText("Date de Naissance : "+userJSON.get("dateDeNaissance").toString());
            textViewNbHeures.setText("Sexe : "+userJSON.getString("sexe"));
            textViewNbHeuresMois.setText("Fonction : "+userJSON.getString("fonction"));
            textViewNbAbsences.setText("Telephone : "+userJSON.getString("telephone"));

        }catch (JSONException e)
        {
            Log.e("Json",e.toString());
        }

    }



}
