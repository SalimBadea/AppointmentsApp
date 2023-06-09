package com.example.tasksapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tasksapp.models.Appointment;
import com.example.tasksapp.R;
import com.example.tasksapp.SharedPreferencesUtilities;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClicked {

    RecyclerView rvAppointments;
    List<Appointment> moduelList;
    List<Appointment> moduelList1;
    List<Appointment> sortedList;

    MyAppointmentsAdapter myAppointmentsAdapter;
    ImageView plus, delete;
    Gson gson;
    String appointments;

    private Button mDismissButton;
    private Button mCancelButton;
    private Button mConfirmButton;
    private Button mDetailsButton;

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

        Collections.reverse(moduelList);

        moduelList1.clear();
        for (int i = 0; i < moduelList.size(); i++) {
            Appointment appointment = new Appointment();
            appointment.setName(moduelList.get(i).getName());
            appointment.setTime(moduelList.get(i).getTime());
            appointment.setNoticeTime(moduelList.get(i).getNoticeTime());
            appointment.setDate(moduelList.get(i).getDate());
            appointment.setNoticeDate(moduelList.get(i).getNoticeDate());
            appointment.setDateTime(moduelList.get(i).getDateTime());
            appointment.setType(moduelList.get(i).getType());
            appointment.setNotes(moduelList.get(i).getNotes());
            appointment.setImportance(moduelList.get(i).getImportance());
            moduelList1.add(i, appointment);
        }
        Log.e("TAG", "onCreate: List >> " + moduelList1.size());

//        Collections.sort(moduelList1, new sortTimeCompare());

//        Collections.sort(moduelList1, new SortDateAndTimeComparator());
//
//        Collections.reverse(moduelList1);

        sortedList.clear();
        for (Appointment dateStr : moduelList1) {
            sortedList.add(dateStr);
            System.out.println("reverse Sorted List >> " + sortedList.size());
        }

//        Collections.sort(sortedList, new sortTimeCompare());

//        Collections.sort(sortedList, new sortDateCompare());
//
//        Collections.reverse(sortedList);

        myAppointmentsAdapter = new MyAppointmentsAdapter(sortedList, this, this);

        rvAppointments = findViewById(R.id.rvAppointments);
        rvAppointments.setHasFixedSize(true);
        rvAppointments.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//        rvAppointments.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvAppointments.setAdapter(myAppointmentsAdapter);
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }

    @Override
    public void onClicked(int position) {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.task_control_dialog);
        dialog.setCancelable(true);
        dialog.show();

        mDismissButton = dialog.findViewById(R.id.btn_dismiss);
        mCancelButton = dialog.findViewById(R.id.btn_cancel);
        mConfirmButton = dialog.findViewById(R.id.btn_confirm);
        mDetailsButton = dialog.findViewById(R.id.btn_details);

        mDismissButton.setOnClickListener(v -> {
            sortedList.remove(position);
            moduelList1.remove(position);
//            myAppointmentsAdapter.removeItem(position);
            moduelList.remove(position);
            moduelList1.addAll(sortedList);

            preferencesUtilities.setAPPOINTMENTS(sortedList);

            Toast.makeText(MainActivity.this, "Appointment is Deleted", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finishAffinity();

            dialog.dismiss();

        });

        mCancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        mConfirmButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        mDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AppointmentDetailsActivity.class);
            intent.putExtra("name", sortedList.get(position).getName());
            intent.putExtra("type", sortedList.get(position).getType());
            intent.putExtra("date", sortedList.get(position).getDate());
            intent.putExtra("noticeDate", sortedList.get(position).getNoticeDate());
            intent.putExtra("time", sortedList.get(position).getTime());
            intent.putExtra("noticeTime", sortedList.get(position).getNoticeTime());
            intent.putExtra("notes", sortedList.get(position).getNotes());
            intent.putExtra("priority", sortedList.get(position).getImportance());
            intent.putExtra("index", position);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            dialog.dismiss();
        });

    }

    class sortDateCompare implements Comparator<Appointment> {
        @Override
        // Method of this class
        public int compare(Appointment a, Appointment b) {
            /* Returns sorted data in Descending order */
            Log.e("MainActivity", "Date >> " + a.getDate() + " >> " + b.getDate());
            return a.getDate().compareTo(b.getDate());
        }
    }

    public static class SortDateAndTimeComparator implements Comparator<Appointment> {
        @Override
        public int compare(Appointment obj1, Appointment obj2) {
            int dateComparison = obj1.getDate().compareTo(obj2.getDate());
            if (dateComparison != 0) {
                Log.e("MainActivity", "dateComparison >> " + dateComparison);
                return dateComparison;
            }

            Log.e("MainActivity", "dateComparison >> " + dateComparison);
            return obj1.getTime().compareTo(obj2.getTime());
        }
    }

    class sortTimeCompare implements Comparator<Appointment> {
        @Override
        // Method of this class
        public int compare(Appointment a, Appointment b) {
            /* Returns sorted data in Descending order */
//            Collections.sort(sortedList, new sortDateCompare());
            Log.e("MainActivity", "Time >> " + a.getTime() + " >> " + b.getTime());
            return a.getTime().compareTo(b.getTime());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        moduelList.clear();
        if (preferencesUtilities.getAPPOINTMENTS() != null)
            moduelList = preferencesUtilities.getAPPOINTMENTS();
        else moduelList = new ArrayList<>();

        Collections.reverse(moduelList);

        moduelList1.clear();
        for (int i = 0; i < moduelList.size(); i++) {
            Appointment appointment = new Appointment();
            appointment.setName(moduelList.get(i).getName());
            appointment.setTime(moduelList.get(i).getTime());
            appointment.setNoticeTime(moduelList.get(i).getNoticeTime());
            appointment.setDate(moduelList.get(i).getDate());
            appointment.setNoticeDate(moduelList.get(i).getNoticeDate());
            appointment.setDateTime(moduelList.get(i).getDateTime());
            appointment.setType(moduelList.get(i).getType());
            appointment.setNotes(moduelList.get(i).getNotes());
            appointment.setImportance(moduelList.get(i).getImportance());
            moduelList1.add(i, appointment);
        }

//        Collections.sort(moduelList1, new sortTimeCompare());

//        Collections.sort(moduelList1, new SortDateAndTimeComparator());
//
//        Collections.reverse(moduelList1);

        sortedList.clear();
        for (Appointment dateStr : moduelList1) {
            sortedList.add(dateStr);
            System.out.println("reverse Sorted List >> " + sortedList.size());
        }

//        Collections.sort(sortedList, new sortTimeCompare());

//        Collections.sort(sortedList, new sortDateCompare());
//
//        Collections.reverse(sortedList);

        myAppointmentsAdapter = new MyAppointmentsAdapter(sortedList, this, this);

        rvAppointments = findViewById(R.id.rvAppointments);
        rvAppointments.setHasFixedSize(true);
        rvAppointments.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvAppointments.setAdapter(myAppointmentsAdapter);

    }
}