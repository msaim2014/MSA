package com.example.android.msa_at_fau_v3;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */

public class EventFragment extends Fragment {
    public static TextView eventData;
    ArrayList<anEvent> allEventsPassed;
    String calendarEvents="";
    String combinedInfo = "";
    RecyclerView recyclerView;
    anEventAdapter adapter;

    public EventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //eventData = (TextView) view.findViewById(R.id.events);
        Context context = getActivity();
        FetchCalendarData process = new FetchCalendarData(context);
        process.execute();
        allEventsPassed = loadData();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new anEventAdapter(context, allEventsPassed);
        recyclerView.setAdapter(adapter);

    }

    private ArrayList<anEvent> loadData(){
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("share preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<anEvent>>() {}.getType();
        allEventsPassed = gson.fromJson(json, type);

        if(allEventsPassed==null){
            allEventsPassed = new ArrayList<>();
        }
        else{
            //displayEvents(allEventsPassed);
        }

        return allEventsPassed;
    }

    public void displayEvents(ArrayList<anEvent> passedEvents){
        for (int i=0; i<passedEvents.size(); i++){
            String eventTitle = passedEvents.get(i).title;
            String eventDescription = passedEvents.get(i).description;
            String eventLocation = passedEvents.get(i).location;
            String eventStart = passedEvents.get(i).start;
            String eventEnd = passedEvents.get(i).end;
            combinedInfo= combinedInfo + eventTitle + "\n"+
                    eventDescription + "\n"+
                    eventLocation + "\n"+
                    eventStart + "\n"+
                    eventEnd + "\n"+ "_____________________"+"\n\n";
        }

        eventData.setText(combinedInfo);
    }

}
