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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvAppointments;
    List<Appointment> moduelList;
    List<Appointment> moduelList1;
    List<Appointment> sortedList;

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
        sortedList = new ArrayList<>();

        moduelList.clear();
        moduelList1.clear();
        sortedList.clear();

        plus = findViewById(R.id.ivAdd);
        delete = findViewById(R.id.delete);

        sortedList.clear();

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

        if (preferencesUtilities.getAPPOINTMENTS() != null) {
            moduelList = preferencesUtilities.getAPPOINTMENTS();
        } else
            moduelList = new ArrayList<>();

        moduelList1.clear();
        for (int i = 0; i < moduelList.size(); i++) {
            Appointment appointment = new Appointment();
            appointment.setName(moduelList.get(i).getName());
            appointment.setTime(moduelList.get(i).getTime());
            appointment.setNoticeTime(moduelList.get(i).getNoticeTime());
            appointment.setDate(moduelList.get(i).getDate());
            appointment.setNoticeDate(moduelList.get(i).getNoticeDate());
            appointment.setType(moduelList.get(i).getType());
            appointment.setNotes(moduelList.get(i).getNotes());
            appointment.setImportance(moduelList.get(i).getImportance());
            moduelList1.add(i, appointment);
        }
        Log.e("TAG", "onCreate: List >> " + moduelList1.size());

        System.out.println("---> Date & Time List After Sort (MM/dd/yyyy '@'hh:mm a)");

//        Collections.sort(moduelList1, new sortDateCompare());

        Collections.sort(moduelList1, new sortTimeCompare());

//        Collections.reverse(moduelList1);

        for (Appointment dateStr : moduelList1) {
            sortedList.add(dateStr);
            System.out.println("reverse Sorted List >> " + sortedList.size());
        }

        myAppointmentsAdapter = new MyAppointmentsAdapter(sortedList, this);

        rvAppointments = findViewById(R.id.rvAppointments);
        rvAppointments.setHasFixedSize(true);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvAppointments.setAdapter(myAppointmentsAdapter);
    }

    class sortDateCompare implements Comparator<Appointment> {
        @Override
        // Method of this class
        public int compare(Appointment a, Appointment b) {
            /* Returns sorted data in Descending order */
            return b.getDate().compareTo(a.getDate());
        }
    }

    class sortTimeCompare implements Comparator<Appointment> {
        @Override
        // Method of this class
        public int compare(Appointment a, Appointment b) {
            /* Returns sorted data in Descending order */
            return b.getTime().compareTo(a.getTime());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        moduelList.clear();
        if (preferencesUtilities.getAPPOINTMENTS() != null)
            moduelList = preferencesUtilities.getAPPOINTMENTS();
        else moduelList = new ArrayList<>();

        moduelList1.clear();
        for (int i = 0; i < moduelList.size(); i++) {
            Appointment appointment = new Appointment();
            appointment.setName(moduelList.get(i).getName());
            appointment.setTime(moduelList.get(i).getTime());
            appointment.setNoticeTime(moduelList.get(i).getNoticeTime());
            appointment.setDate(moduelList.get(i).getDate());
            appointment.setNoticeDate(moduelList.get(i).getNoticeDate());
            appointment.setType(moduelList.get(i).getType());
            appointment.setNotes(moduelList.get(i).getNotes());
            appointment.setImportance(moduelList.get(i).getImportance());
            moduelList1.add(i, appointment);
        }

        Collections.sort(moduelList1, new sortTimeCompare());

//        Collections.reverse(moduelList1);

        sortedList.clear();
        for (Appointment dateStr : moduelList1) {
            sortedList.add(dateStr);
            System.out.println("reverse Sorted List >> " + sortedList.size());
        }

        myAppointmentsAdapter = new MyAppointmentsAdapter(sortedList, this);

        rvAppointments = findViewById(R.id.rvAppointments);
        rvAppointments.setHasFixedSize(true);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvAppointments.setAdapter(myAppointmentsAdapter);

    }
}