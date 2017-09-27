package com.swissport.www.swissport;

import android.content.Context;
import android.content.Intent;


/**
 * Created by USER-hp on 9/13/2017.
 */
public class RoutesManager {

    public void goToLogin(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }
    public void goToProfle(Context context){
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }
    public void changePass(Context context){
        Intent intent = new Intent(context, ChangePassActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }
    public void newRequest(Context context){
        Intent intent = new Intent(context, RequestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }
    public void goToCalendar(Context context){
        Intent intent = new Intent(context, CalendarMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }
    public void goToNotifications(Context context){
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }
}
