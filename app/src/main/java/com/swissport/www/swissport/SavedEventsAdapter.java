package com.swissport.www.swissport;
/**
 * Created by DUAL on 16/09/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maximilian on 9/1/14.
 */
public class SavedEventsAdapter extends BaseAdapter {
    // Variables
    private Context mContext;
    private static ViewHolder mHolder;




    private static class ViewHolder {
        TextView mTitle;
        TextView mAbout;
        ImageView mImageView;
        View mDivider;
    }

    // Constructor
    public SavedEventsAdapter(Context context) {
        mContext = context;



    }

    @Override
    public int getCount() {
        if (MaterialCalendarFragment.mNumEventsOnDay != 0 || MaterialCalendarFragment.mNumEventsOnDay != -1) {
            return MaterialCalendarFragment.mNumEventsOnDay;
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        getPlanning(String.valueOf(MaterialCalendar.mMonth),String.valueOf(MaterialCalendar.mYear));
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_material_saved_event_item, parent, false);

            mHolder = new ViewHolder();

            if (convertView != null) {
                // FindViewById
                mHolder.mTitle = (TextView) convertView.findViewById(R.id.saved_event_title_textView);
                mHolder.mAbout = (TextView) convertView.findViewById(R.id.saved_event_about_textView);
                mHolder.mImageView = (ImageView) convertView.findViewById(R.id.saved_event_imageView);
                convertView.setTag(mHolder);
            }
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        // Animates in each cell when added to the ListView
        Animation animateIn = AnimationUtils.loadAnimation(mContext, R.anim.listview_top_down);
        if (convertView != null && animateIn != null) {
            convertView.startAnimation(animateIn);
        }

        if (mHolder.mTitle != null ) {
            setEventTitle();

        }


        if (mHolder.mAbout != null) {
            setEventAbout();
        }

        if (mHolder.mImageView != null) {
            setEventImage();

        }

        return convertView;
    }

    private void setEventTitle() {
        Log.e("title index",String.valueOf(MaterialCalendar.daySelected));
        mHolder.mTitle.setText(MaterialCalendar.planning.get(MaterialCalendar.daySelected-1));//on soustrait 1 car les valeurs dans le planning commence à partir de l'indice 0
    }

    private void setEventAbout() {
        String heureDebut="";
        String heureFin="";
        if ( MaterialCalendar.codes !=null){
            for (int i=0;i<MaterialCalendar.codes.length();i++){
                try{
                    JSONObject jsonObj = MaterialCalendar.codes.getJSONObject(i);
                    String code=jsonObj.getString("code");
                    if ( code.trim().equals(MaterialCalendar.planning.get(MaterialCalendar.daySelected-1))){

                        heureDebut=jsonObj.getString("heureDebut");
                        heureFin=jsonObj.getString("heureFin");
                        i=MaterialCalendar.codes.length();
                    }

                }catch (JSONException e) {
                    Log.e("Main json exc",e.toString());
                }

            }
        }
        mHolder.mAbout.setText(" Heure début : "+heureDebut+" \n Heure fin :" +heureFin) ;
    }

    private void setEventImage() {
        // Set event item image (bitmap, drawable, uri...)
        mHolder.mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_calendar));
    }





}
