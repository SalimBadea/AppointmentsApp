package com.example.tasksapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.tasksapp.models.Appointment;
import com.example.tasksapp.R;
import com.example.tasksapp.SharedPreferencesUtilities;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvAppointments;
    List<Appointment> moduelList;
    List<Appointment> moduelList1;

    MyAppointmentsAdapter myAppointmentsAdapter;
    ImageView plus, delete;
    Gson gson;
    String appointments;

    SharedPreferencesUtilities preferencesUtilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferencesUtilities = new SharedPreferencesUtilities(this);

        moduelList = new ArrayList<>();
        moduelList1 = new ArrayList<>();

        plus = findViewById(R.id.ivAdd);
        delete = findViewById(R.id.delete);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddAppointmentActivity.class));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduelList1.clear();
                myAppointmentsAdapter.clearList();
                preferencesUtilities.setAPPOINTMENTS(moduelList1);

                if (preferencesUtilities.getAPPOINTMENTS() != null)
                    moduelList = preferencesUtilities.getAPPOINTMENTS();
                else moduelList = new ArrayList<>();
            }
        });

        if (preferencesUtilities.getAPPOINTMENTS() != null)
            moduelList = preferencesUtilities.getAPPOINTMENTS();
        else moduelList = new ArrayList<>();

        for (int i = 0; i < moduelList.size(); i++) {
            Appointment appointment = new Appointment();
            appointment.setName(moduelList.get(i).getName());
            appointment.setTime(moduelList.get(i).getTime());
            appointment.setDate(moduelList.get(i).getDate());
            appointment.setType(moduelList.get(i).getType());
            appointment.setNotes(moduelList.get(i).getNotes());
            appointment.setImportance(moduelList.get(i).getImportance());
            moduelList1.add(i, appointment);
        }
        Log.e("TAG", "onCreate: List >> " + moduelList1.size());

        myAppointmentsAdapter = new MyAppointmentsAdapter(moduelList1, this);

        rvAppointments = findViewById(R.id.rvAppointments);
        rvAppointments.setHasFixedSize(true);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvAppointments.setAdapter(myAppointmentsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        moduelList1.clear();
        if (preferencesUtilities.getAPPOINTMENTS() != null)
            moduelList = preferencesUtilities.getAPPOINTMENTS();
        else moduelList = new ArrayList<>();

        for (int i = 0; i < moduelList.size(); i++) {
            Appointment appointment = new Appointment();
            appointment.setName(moduelList.get(i).getName());
            appointment.setTime(moduelList.get(i).getTime());
            appointment.setDate(moduelList.get(i).getDate());
            appointment.setType(moduelList.get(i).getType());
            appointment.setNotes(moduelList.get(i).getNotes());
            appointment.setImportance(moduelList.get(i).getImportance());
            moduelList1.add(i, appointment);
        }

        myAppointmentsAdapter = new MyAppointmentsAdapter(moduelList1, this);

        rvAppointments = findViewById(R.id.rvAppointments);
        rvAppointments.setHasFixedSize(true);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvAppointments.setAdapter(myAppointmentsAdapter);

    }
}