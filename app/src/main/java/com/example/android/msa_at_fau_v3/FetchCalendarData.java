package com.example.android.msa_at_fau_v3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import android.content.Context;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class FetchCalendarData extends AsyncTask<Void, Void, Void> {
    String calendarEvents="";
    public List<anEvent> allEvents;
    Context context;

    public FetchCalendarData(Context sentContext){
        this.context=sentContext;
    }

    protected Void doInBackground(Void... voids) {
        try {
            URL url=new URL("https://www.googleapis.com/calendar/v3/calendars/msaim1998@gmail.com/events?key=AIzaSyBGVPlYhiBkPAC_IIe13JpbJ9fy841SK2g");
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line="";
            while(line!=null){
                //read line
                line=bufferedReader.readLine();
                calendarEvents=calendarEvents+line;
            }

            JSONObject jsonObject = new JSONObject(calendarEvents);
            JSONArray items = jsonObject.getJSONArray("items");
            JSONObject events = items.getJSONObject(0);
            //JSONObject start = events.getJSONObject("start");
            //JSONObject end = events.getJSONObject("end");

            allEvents = new ArrayList<anEvent>();
            for(int i = items.length()-1; i>items.length()-16; i--) {
                anEvent singleEvent = new anEvent();
                try {
                    singleEvent.title = items.getJSONObject(i).getString("summary");
                } catch (Exception e) {
                    singleEvent.title = "";
                }
                try {
                    singleEvent.description = "Description: " + items.getJSONObject(i).getString("description");
                } catch (Exception e) {
                    singleEvent.description = "";
                }
                try {
                    singleEvent.location = "Location: " + items.getJSONObject(i).getString("location");
                } catch (Exception e) {
                    singleEvent.location = "";
                }
                try {
                    singleEvent.start = items.getJSONObject(i).getJSONObject("start").getString("dateTime");
                    singleEvent.start = "Start: " + convertDate(singleEvent.start);
                } catch (Exception e) {
                    singleEvent.start = "";
                }
                try {
                    singleEvent.end = items.getJSONObject(i).getJSONObject("end").getString("dateTime");
                    singleEvent.end = "End: " + convertDate(singleEvent.end);
                } catch (Exception e) {
                    singleEvent.end = "";
                }
                allEvents.add(singleEvent);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("share preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(allEvents);
        editor.putString("task list", json);
        editor.apply();
    }

    public String convertDate(String date){
        String final_date="";

        SimpleDateFormat original_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            //converting string to date format
            Date converted_date = original_format.parse(date);
            SimpleDateFormat new_format = new SimpleDateFormat("MMMM dd, yyyy hh:mm");
            final_date = new_format.format(converted_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return final_date;
    }

}