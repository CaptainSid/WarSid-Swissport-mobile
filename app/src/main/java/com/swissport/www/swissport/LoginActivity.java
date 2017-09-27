package com.swissport.www.swissport;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String LOGIN_URL = "http://10.198.0.199:3000/api/mobileLogin";
//    public static final String LOGIN_URL = "http://192.168.1.4:3000/api/mobileLogin";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);
    }



    private void userLogin() {
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        StringRequest POSTRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resJSON = new JSONObject(response);
                            String message = resJSON.getString("message");
                            if ( message.trim().equals("success"))
                            {
                                JSONObject user= (JSONObject) resJSON.get("result");
                                SharedPreferencesManager Manager=new SharedPreferencesManager();
                                Manager.saveSharedPreferences("user",user.toString(),getApplicationContext());
                                Context context=LoginActivity.this;
                                Intent NotificationServic = new Intent(context, SocketManager.class);
                                context.startService(NotificationServic);
                                RoutesManager route=new RoutesManager();
                                route.goToProfle(getBaseContext());

                            }else{
                                Toast.makeText(LoginActivity.this,message, Toast.LENGTH_LONG).show();
                            }

                        }
                        catch (JSONException e) {
                            Toast.makeText(LoginActivity.this,e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(KEY_EMAIL, email);
                map.put(KEY_PASSWORD, password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(POSTRequest);
    }


    @Override
    public void onClick(View v) {
        if ( editTextEmail.getText().toString().trim().isEmpty() || editTextPassword.getText().toString().trim().isEmpty())
        {
            Toast.makeText(LoginActivity.this,"Attention ! vous devez d'abord saisir votre email et votre mot de passe", Toast.LENGTH_LONG).show();
        }else {
            if ( android.util.Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString().trim()).matches())
            {
                userLogin();
            }else {
                Toast.makeText(LoginActivity.this,"Veuillez saisir une adresse mail valide", Toast.LENGTH_LONG).show();
            }

        }
    }


}

