package com.swissport.www.swissport;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.net.URISyntaxException;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.renderscript.RenderScript;
import android.support.v4.app.NotificationCompat;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by USER-hp on 9/17/2017.
 */
public class SocketManager extends IntentService {
   public static Socket mSocket;
    private Intent myintent;
//constructeur
    public SocketManager() {
        super("SocketManager");
    }
    private void sendAck (final int notificationId){
        Thread thread = new Thread() {
            @Override
            public void run() {
                mSocket.emit("sendAck",notificationId);
            }
        };
        thread.start();

    }

    private Emitter.Listener onNotification=new Emitter.Listener(){
        @Override
        public void call(final Object... args) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getApplicationContext());

//Create the intent that’ll fire when the user taps the notification//
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                    mBuilder.setContentIntent(pendingIntent);
                    mBuilder.setPriority(Notification.PRIORITY_MAX);
                    mBuilder.setSmallIcon(R.drawable.notification_icon);
                    mBuilder.setContentTitle("Swissport");
                    JSONObject data = (JSONObject) args[0];
                    int id=0;
                    try {
                        String notificationText=data.getString("text");
                        id=data.getInt("id");
                        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText));
                        mBuilder.setContentText(notificationText);
                        Log.e("socket manager",notificationText);
                    } catch (JSONException e) {
                        Log.e("socket manager",e.toString());
                    }
                    mBuilder.setVibrate(new long[] { 1000, 1000});
                    mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
                    mBuilder.setAutoCancel(true);
                    NotificationManager mNotificationManager =

                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                    mNotificationManager.notify(id, mBuilder.build());
                    sendAck(id);

                }
            };
            thread.start();

            Log.e("listner","socket recieved");
        }
    };

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            myintent=intent;
            Log.e("socket Manager", "On Handle Intent");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if (preferences.contains("user"))
                {
                    String user = preferences.getString("user", "");
                    try{
                        JSONObject userJSON = new JSONObject(user);
                        String matricule=userJSON.getString("matricule");
//                        JSONObject query=new JSONObject("{auth_token="+matricule+",fonction=agent}");
                        try {
                            IO.Options opts = new IO.Options();
                            opts.query = "auth_token="+matricule;
//                            opts.query=query.toString();
//                            mSocket = IO.socket("http://10.198.0.199:3000", opts);
                            mSocket = IO.socket("http://10.198.0.199:3000", opts);
                            mSocket.connect();
                            Log.e("connection :", "connection attempt done");
                        }catch (URISyntaxException e) {
                            Log.e("URISyntaxException:", e.toString());
                            e.printStackTrace();
                        }
                    }
                    catch (JSONException e) {
                        Log.e("Main json ect",e.toString());
                    }
                }
                else {
                    Log.e("socket Manager","no sharedPreferences found");
                }



            mSocket.on("notification",onNotification);
            mSocket.on(Socket.EVENT_RECONNECT, onReconnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
//            mSocket.on(Config.ON_JOINED, onJoined);
//            mSocket.on(Config.ON_RECONNECTED, onReconnected);
        }
    }

    private Emitter.Listener onReconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Log.e("socket Manager", "On Reconnect");
//                    mSocket.connect();

                }
            };
            thread.start();
        }
    };

    @Override
    public void onDestroy() {
//        super.onDestroy();
//        Log.e("Socket Manager", "Destroyed");
    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Log.e("socket Manager"," deconnxtion");
                    if ( mSocket != null)
                    {
                        mSocket.disconnect();
                        mSocket.off();
                        mSocket.close();
                        mSocket=null;
                    }
                    SocketManager.this.stopService(myintent);
//                    stopSelf();
                }
            };
            thread.start();
        }
    };

}
