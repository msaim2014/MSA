package com.example.android.msa_at_fau_v3;


import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    Toolbar toolbar;
    CardView prayerTime;
    CardView compass;
    CardView events;
    CardView settings;
    CardView email;
    CollapsingToolbarLayout toolbarLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolBarId);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        prayerTime = (CardView) view.findViewById(R.id.time_card);
        compass = (CardView) view.findViewById(R.id.compass_card);
        events = (CardView) view.findViewById(R.id.events_card);
        settings = (CardView) view.findViewById(R.id.settings_card);
        email = (CardView)view.findViewById(R.id.email_card);

        toolbarLayout = view.findViewById(R.id.collapsingToolBar);
        Typeface customFont = ResourcesCompat.getFont(this.getContext(),R.font.boxing);
        toolbarLayout.setCollapsedTitleTypeface(customFont);
        toolbarLayout.setExpandedTitleTypeface(customFont);
        //toolBarText = (TextView) view.findViewById(R.id.collapsingToolBar);

        //Typeface customFont = Typeface.createFromFile("font/boxing.otf");
        //toolBarText.setTypeface(customFont);

        prayerTime.setOnClickListener(this);
        compass.setOnClickListener(this);
        events.setOnClickListener(this);
        settings.setOnClickListener(this);
        email.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewNum = view.getId();
        if(viewNum == R.id.time_card){
            TimesFragment times = new TimesFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.replace, times).addToBackStack(null).commit();
        }
        else if(viewNum == R.id.compass_card){
            Compass2Fragment compass = new Compass2Fragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.replace, compass).addToBackStack(null).commit();
        }
        else if(viewNum == R.id.events_card){
            EventFragment events = new EventFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.replace, events).addToBackStack(null).commit();
        }
        else if(viewNum == R.id.settings_card){
            SettingsFragment settings = new SettingsFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.replace, settings).addToBackStack(null).commit();
        }
        else if(viewNum == R.id.email_card){
            EmailFragment email = new EmailFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.replace, email).addToBackStack(null).commit();
        }
    }

}
