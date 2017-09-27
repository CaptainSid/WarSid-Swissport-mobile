package com.swissport.www.swissport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.toolbox.Volley;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private Button buttonLogout;
//    public static final String USER_URL = "http://192.168.1.4:3000/api/mobileCheckUser";
    public static final String USER_URL = "http://10.198.0.199:3000/api/mobileCheckUser";
    public static final String Plan_URL = "http://10.198.0.199:3000/api/mobileGetPlanning";
    public static final String CODES_URL = "http://10.198.0.199:3000/api/mobileGetCodes";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MAT = "matricule";
    private String email;
    public static String matricule;
    private ProgressDialog dialog;
    public static Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext=getApplicationContext();
        dialog=new ProgressDialog(MainActivity.this);
        dialog.setMessage("Veuillez patienter quelques instants");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.contains("user"))
        {
            String user = preferences.getString("user", "");
            try{
                JSONObject userJSON = new JSONObject(user);
                String userEmail=userJSON.getString("email");
                matricule=userJSON.getString("matricule");
                checkUser(userEmail);
            }
            catch (JSONException e) {
                Log.e("Main json ect",e.toString());
            }


        }else{
            dialog.hide();
            RoutesManager route=new RoutesManager();
            route.goToLogin(getBaseContext());
        }





    }




    @Override
    public void onClick(View v) {
    }

    private void checkUser(String userEmail){
        email=userEmail;
        StringRequest POSTRequest = new StringRequest(Request.Method.POST, USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resJSON = new JSONObject(response);
                            String message = resJSON.getString("message");
                            SharedPreferencesManager Manager=new SharedPreferencesManager();
                            RoutesManager route=new RoutesManager();
                            if ( message.trim().equals("success"))
                            {
                                String user=  resJSON.get("result").toString();
                                Manager.saveSharedPreferences("user",user,getApplicationContext());
                                Context context=MainActivity.this;
                                Intent NotificationServic = new Intent(context, SocketManager.class);
                                context.startService(NotificationServic);
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                if (! preferences.contains("codes"))
                                {
                                    getCodes();
                                }
                                getPlanning();
                            }else{
                              Manager.deleteSharedPreference(getApplicationContext());
                                dialog.hide();
                                route.goToLogin(getBaseContext());

                            }

                        }
                        catch (JSONException e) {
                            Log.e("Main json exc",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Main volley",error.toString());
                        // send him to see his planning
                        dialog.hide();
                        RoutesManager route=new RoutesManager();
                        Toast.makeText(MainActivity.this,error.toString(), Toast.LENGTH_LONG).show();
//                        route.goToProfle(getBaseContext());
                        route.goToCalendar(getBaseContext());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(KEY_EMAIL, email);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(POSTRequest);
    }

    private void getCodes(){
        StringRequest POSTRequest = new StringRequest(Request.Method.GET, CODES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("getcodes","i am in response");
                            JSONObject resJSON = new JSONObject(response);
                            String message = resJSON.getString("message");
                            SharedPreferencesManager Manager=new SharedPreferencesManager();
                            if ( message.trim().equals("success"))
                            {
                                JSONObject tmp=(JSONObject) resJSON.get("result");
                                JSONArray allCodes=  (JSONArray) tmp.get("allCodes");
                                Manager.saveSharedPreferences("codes",allCodes.toString(),MainActivity.this);
//                                for (int i=0;i<allCodes.length();i++)
//                                {
//                                    JSONObject objet=(JSONObject)allCodes.get(i);
////                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//                                        Log.e("Mainactivity","dummy Test:"+"code = " +objet.getString("code")+"heure debut= "+objet.getString("heureDebut")+" heurefin= "+objet.getString("heureFin"));
////                                    Manager.saveSharedPreferences(Tag,plan.toString(),MainActivity.this);
//                                }

                            }

                        }
                        catch (JSONException e) {
                            Log.e("Main json exc",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Main volley",error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(POSTRequest);
    }


    private void getPlanning(){
//cette méthode sauvegarder les plannings de l'utilisateur dans un fichier
        StringRequest POSTRequest = new StringRequest(Request.Method.POST, Plan_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<String> planning;
                            JSONObject resJSON = new JSONObject(response);
                            String message = resJSON.getString("message");
                            SharedPreferencesManager Manager=new SharedPreferencesManager();
                            if ( message.trim().equals("success"))
                            {
                                JSONArray allPlannings=  (JSONArray) resJSON.get("result");
                                for (int i=0;i<allPlannings.length();i++)
                                {
                                    JSONObject objet=(JSONObject)allPlannings.get(i);
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                    String Tag="planning"+objet.getInt("mois")+"/"+objet.getInt("annee");
                                    Log.e("MainActivity Tag",Tag);
                                        JSONArray plan=(JSONArray)objet.get("planning");
//                                        Log.e("Mainactivity","dummy Test:"+plan.get(0));
                                        Manager.saveSharedPreferences(Tag,plan.toString(),MainActivity.this);
                                }

                            }else{
                                RoutesManager route = new RoutesManager();
                                route.goToCalendar(getBaseContext());
                                    //send him to mai Calendar activity
                            }
                            dialog.hide();
                            RoutesManager route = new RoutesManager();
//                            route.goToProfle(getBaseContext());
                            route.goToCalendar(getBaseContext());
                        }
                        catch (JSONException e) {
                            Log.e("Main json exc",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Main volley",error.toString());
                        // send him to see his planning
                        dialog.hide();
                        Toast.makeText(MainActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                        RoutesManager route = new RoutesManager();
//                        route.goToProfle(getBaseContext());
                        route.goToCalendar(getBaseContext());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                if ( matricule != null)
                {
                    map.put(KEY_MAT,matricule);
                }
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(POSTRequest);
    }

}

