package com.example.android.msa_at_fau_v3;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimesFragment extends Fragment {

    public static String status="Times";
    public static TextView location;
    public static TextView date;
    public static TextView fajr;
    public static TextView dhur;
    public static TextView asr;
    public static TextView maghrib;
    public static TextView isha;

    public static final String TEXT = "text";
    public static final String SHARED_PREFS="sharedPrefs";

    public TimesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_times, container, false);

    }

    @Override
    //triggered after onCreateView
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        location = (TextView) view.findViewById(R.id.location);
        date = (TextView) view.findViewById(R.id.date);
        fajr = (TextView) view.findViewById(R.id.fajr);
        dhur = (TextView) view.findViewById(R.id.dhur);
        asr = (TextView) view.findViewById(R.id.asr);
        maghrib = (TextView) view.findViewById(R.id.maghrib);
        isha = (TextView) view.findViewById(R.id.isha);

        String city_name = loadData();
        fetchData process = new fetchData(status, city_name);
        process.execute();
    }

    public String loadData(){
        SharedPreferences mSharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        String name = mSharedPreferences.getString(TEXT, "Default");
        return name;
    }
}
