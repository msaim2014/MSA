package com.example.android.msa_at_fau_v3;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


        fetchData process=new fetchData(status);
        process.execute();
    }
}
