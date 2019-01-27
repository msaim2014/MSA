package com.example.android.msa_at_fau_v3;

import android.content.Context;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Date;

public class fetchData extends AsyncTask<Void, Void, Void> {
    private String data="";
    private String location;
    private String api_key="8f16e85e5ef77cdcc1a61e6688f6483f";
    private String qiblaDirection="";
    private String status="";
    private String[] parsed;
    public String full_location;
    public boolean city_validity;
    public prayerData info;
    private String [] months;

    public fetchData(String status, String city_name){
        this.status=status;
        this.parsed = new String[11];
        this.location = city_name;
        prayerData info = new prayerData();
    }

    @Override //background thread
    protected Void doInBackground(Void... voids) {
        try {
            //http://muslimsalat.com/boca raton.json?key=api_key8f16e85e5ef77cdcc1a61e6688f6483f
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
            parsed[1] = convertDate(parsed[1]);
            parsed[2] = String.format("%-15s %33s", "Fajr:", prayerTimes.getString("fajr"));
            parsed[3] = String.format("%-15s %31s", "Dhur:", prayerTimes.getString("dhuhr"));
            parsed[4] = String.format("%-15s %34s", "Asr:", prayerTimes.getString("asr"));
            parsed[5] = String.format("%-15s %29s", "Maghrib:", prayerTimes.getString("maghrib"));
            parsed[6] = String.format("%-15s %34s", "Isha:", prayerTimes.getString("isha"));

            //parsed[2] = String.format("%-15s %33s", "Fajr:", "6:02 am");
            //parsed[3] = String.format("%-15s %31s", "Dhur:", "12:27 pm");
            //parsed[4] = String.format("%-15s %34s", "Asr:", "3:24 pm");
            //parsed[5] = String.format("%-15s %28s", "Maghrib:", "5:48 pm");
            //parsed[6] = String.format("%-15s %34s", "Isha:", "6:52 pm");

            parsed[7] = (String) jsonObj.get("city");
            parsed[8] = (String) jsonObj.get("state");
            parsed[9] = (String) jsonObj.get("country");
            parsed[10] = (String) jsonObj.get("status_description");
            this.city_validity = checkValidity(parsed[10]);

            this.full_location = parsed[7] + ", " + parsed[8] + ", " + parsed[9];

            qiblaDirection = "Qibla Azimuth From "+ "\n" +
                             jsonObj.get("title") + ": " +
                             jsonObj.getString("qibla_direction");

            /*info.setFull_location(this.full_location);
            info.setDate_for(parsed[1]);
            info.setFajr(parsed[2]);
            info.setDhur(parsed[3]);
            info.setAsr(parsed[4]);
            info.setMaghrib(parsed[5]);
            info.setIsha(parsed[6]);
            info.setStatus_description(parsed[10]);
            info.setQibla_direction(qiblaDirection);*/

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

            UnderlineSpan underlineSpan = new UnderlineSpan();

            SpannableString fajr_string = new SpannableString(this.parsed[2]);
            SpannableString dhur_string = new SpannableString(this.parsed[3]);
            SpannableString asr_string = new SpannableString(this.parsed[4]);
            SpannableString maghrib_string = new SpannableString(this.parsed[5]);
            SpannableString isha_string = new SpannableString(this.parsed[6]);

            fajr_string.setSpan(underlineSpan, 0, this.parsed[2].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            dhur_string.setSpan(underlineSpan, 0, this.parsed[3].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            asr_string.setSpan(underlineSpan, 0, this.parsed[4].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            maghrib_string.setSpan(underlineSpan, 0, this.parsed[5].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            isha_string.setSpan(underlineSpan, 0, this.parsed[6].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            TimesFragment.location.setText(this.full_location);
            TimesFragment.date.setText(this.parsed[1]);
            TimesFragment.fajr.setText(fajr_string);
            TimesFragment.dhur.setText(dhur_string);
            TimesFragment.asr.setText(asr_string);
            TimesFragment.maghrib.setText(maghrib_string);
            TimesFragment.isha.setText(isha_string);
        }
        else if (this.status=="Compass"){
            Compass2Fragment.qiblaDirection.setText(this.qiblaDirection);
        }
        else if (this.status=="Settings"){
            if(this.city_validity == false){
                SettingsFragment.edit_box.setError("No information regarding typed city");
                SettingsFragment.city.setText("Location: N/A");
            }

            else{
                SettingsFragment.city.setText("Location: " + full_location);
            }
        }
    }

    public boolean checkValidity(String input){
        if(input.equals("Success.")){
            return true;
        }
        else{
            return false;
        }
    }

    public String convertDate(String date){
        String[] months = {"none", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String final_date="";

        SimpleDateFormat original_format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //converting string to date format
            Date converted_date = original_format.parse(date);
            SimpleDateFormat new_format = new SimpleDateFormat("MMMM dd, yyyy");
            final_date = new_format.format(converted_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return final_date;
    }
}