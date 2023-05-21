package com.example.tasksapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.tasksapp.models.Appointment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class SharedPreferencesUtilities {

    SharedPreferences sharedPreferences;

    public SharedPreferencesUtilities(Context context) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }    ///// create the key name for what you want to save in shared

    private static String APPOINTMENTS = "APPOINTMENTS";

    /// now create a setter and getter to edit the value and to get it


    public void setAPPOINTMENTS(List<Appointment> appointments) {
        Gson gson = new Gson();
        String json = gson.toJson(appointments);

        sharedPreferences.edit().putString(APPOINTMENTS, json).commit();

    }

    public List<Appointment> getAPPOINTMENTS() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(APPOINTMENTS, "");
        Type type = new TypeToken<List<Appointment>>() {}.getType();

        return gson.fromJson(json, type);
    }

    public void clearData(){
        sharedPreferences.edit().clear();
        sharedPreferences.edit().commit();
    }


}
