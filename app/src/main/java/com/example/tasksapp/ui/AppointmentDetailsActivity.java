package com.example.tasksapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tasksapp.R;
import com.example.tasksapp.SharedPreferencesUtilities;
import com.example.tasksapp.models.Appointment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentDetailsActivity extends AppCompatActivity {

    TextView name, type, priority, notes;
    TextView date, time, noticeDate, noticeTime, title, dismiss;
    Button edit;

    List<Appointment> moduelList;
    List<Appointment> moduelList1;

    SharedPreferencesUtilities sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        sharedPreferences = new SharedPreferencesUtilities(this);

        Intent intent = getIntent();
        String mTitle = intent.getStringExtra("name");
        String mType = intent.getStringExtra("type");
        String mDate = intent.getStringExtra("date");
        String mNoticeDate = intent.getStringExtra("noticeDate");
        String mTime = intent.getStringExtra("time");
        String mNoticeTime = intent.getStringExtra("noticeTime");
        String mNotes = intent.getStringExtra("notes");
        String mPriority = intent.getStringExtra("priority");
        int index = intent.getIntExtra("index", 0);

Log.e("Details" , "Details >> " + mNoticeTime + " " + mNoticeDate);
        name = findViewById(R.id.txtName);
        type = findViewById(R.id.txtType);
        priority = findViewById(R.id.txtPriority);
        notes = findViewById(R.id.notes);
        date = findViewById(R.id.txtDate);
        time = findViewById(R.id.txtTime);
        noticeDate = findViewById(R.id.txtNoticeDate);
        noticeTime = findViewById(R.id.txtNoticeTime);
        edit = findViewById(R.id.btn_edit);
        dismiss = findViewById(R.id.btn_dismiss);
        title = findViewById(R.id.title);

        name.setText(mTitle);
        type.setText(mType);
        date.setText(mDate);
        noticeDate.setText(mNoticeDate);
        time.setText(mTime);
        noticeTime.setText(mNoticeTime);
        priority.setText(mPriority);
        notes.setText(mNotes);

        if (sharedPreferences.getAPPOINTMENTS() != null)
            moduelList = sharedPreferences.getAPPOINTMENTS();
        else moduelList = new ArrayList<>();

        moduelList1 = new ArrayList<>();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppointmentDetailsActivity.this, EditAppointmentActivity.class);
                intent.putExtra("name", mTitle);
                intent.putExtra("type", mType);
                intent.putExtra("date", mDate + " " + mTime);
                intent.putExtra("noticeDate", mNoticeDate + " " + mNoticeTime);
                intent.putExtra("notes", mNotes);
                intent.putExtra("priority", mPriority);
                intent.putExtra("index", index);
                startActivity(intent);
                finish();
            }
        });

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppointmentDetailsActivity.this, MainActivity.class));
                finish();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduelList.remove(index);
                moduelList1.addAll(moduelList);

                sharedPreferences.setAPPOINTMENTS(moduelList1);

                Toast.makeText(AppointmentDetailsActivity.this, "Appointment is Deleted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AppointmentDetailsActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}