package com.example.android.msa_at_fau_v3;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    public static TextView city;
    public static EditText edit_box;
    private Button save_button;

    public static final String SHARED_PREFS="sharedPrefs";
    public static final String TEXT = "text";

    private String text;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        city = (TextView) view.findViewById(R.id.city);
        edit_box = (EditText) view.findViewById(R.id.edit_box);
        save_button = (Button) view.findViewById(R.id.save_button);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //city.setText(edit_box.getText().toString());
                saveData();
            }
        });

        loadData();
    }

    public void saveData(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String city_name = edit_box.getText().toString();
        editor.putString(TEXT, city_name);
        editor.apply();

        fetchData save_process = new fetchData("Settings", city_name);
        save_process.execute();
        
        Toast.makeText(this.getActivity(), "Data Saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        text =sharedPreferences.getString(TEXT, "Please add a city");

        fetchData load_process = new fetchData("Settings", text);
        load_process.execute();
    }
}
