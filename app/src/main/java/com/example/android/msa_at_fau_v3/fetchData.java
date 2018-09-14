package com.example.android.msa_at_fau_v3;

import android.os.AsyncTask;

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

public class fetchData extends AsyncTask<Void, Void, Void>{
    String data="";
    String location="Boca Raton";
    String api_key="8f16e85e5ef77cdcc1a61e6688f6483f";
    String qiblaDirection="";
    String status="";
    public String[] parsed = new String[10];

    public fetchData(String status){
        this.status=status;
    }

    @Override //background thread
    protected Void doInBackground(Void... voids) {
        try {
            URL url=new URL("http://muslimsalat.com/" + location + ".json?key=api_key"+api_key);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line="";
            while(line!=null){
                //read line
                line=bufferedReader.readLine();
                data=data+line;
            }

            //converting date format
//            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
//            DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
//            String inputDateStr="2013-06-24";
//            Date date = inputFormat.parse(inputDateStr);
//            String outputDateStr = outputFormat.format(date);

            JSONObject jsonObj = new JSONObject(data);
            JSONArray items = jsonObj.getJSONArray("items");
            JSONObject prayerTimes = items.getJSONObject(0);

            parsed[0] = (String) jsonObj.get("title");
            parsed[1] = prayerTimes.getString("date_for");
            parsed[2] = "fajr: " + prayerTimes.getString("fajr");
            parsed[3] = "Dhur: " + prayerTimes.getString("dhuhr");
            parsed[4] = "Asr: " + prayerTimes.getString("asr");
            parsed[5] = "Maghrib: " + prayerTimes.getString("maghrib");
            parsed[6] = "Isha: " + prayerTimes.getString("isha");

            qiblaDirection = "Qibla Azimuth From "+ "\n" +
                             jsonObj.get("title") + ": " +
                             jsonObj.getString("qibla_direction");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override //UI thread
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(this.status=="Times"){
            TimesFragment.location.setText(this.parsed[0]);
            TimesFragment.date.setText(this.parsed[1]);
            TimesFragment.fajr.setText(this.parsed[2]);
            TimesFragment.dhur.setText(this.parsed[3]);
            TimesFragment.asr.setText(this.parsed[4]);
            TimesFragment.maghrib.setText(this.parsed[5]);
            TimesFragment.isha.setText(this.parsed[6]);
        }
        else if (this.status=="Compass"){
            Compass2Fragment.qiblaDirection.setText(this.qiblaDirection);
        }

    }
}
