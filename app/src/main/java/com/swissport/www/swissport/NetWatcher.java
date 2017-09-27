package com.swissport.www.swissport;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * Created by USER-hp on 9/20/2017.
 */
public class NetWatcher extends BroadcastReceiver {
    private Context myContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        myContext=context;
        Intent NotificationService = new Intent(context, SocketManager.class);
        if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
            NetworkInfo networkInfo =
                    intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            Log.e("Netwatcher", networkInfo.toString());
            if (networkInfo.isConnected()){
                Log.e("Netwatcher", "start service");
                context.startService(NotificationService);
            }
        }else if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo == null){
                    context.stopService(NotificationService);
            }else {
                if ( !activeNetInfo.isConnected()){
                    context.stopService(NotificationService);
                }
            }
        }
    }

}
