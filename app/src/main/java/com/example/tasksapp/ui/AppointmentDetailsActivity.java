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

import com.example.tasksapp.R;
import com.example.tasksapp.models.Appointment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentDetailsActivity extends AppCompatActivity {

    TextView name, type, priority, notes;
    TextView date, time, title;
    Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        Intent intent = getIntent();
        String mTitle = intent.getStringExtra("name");
        String mType = intent.getStringExtra("type");
        String mDate = intent.getStringExtra("date");
        String mTime = intent.getStringExtra("time");
        String mNotes = intent.getStringExtra("notes");
        String mPriority = intent.getStringExtra("priority");
        int index = intent.getIntExtra("index", 0);


        name = findViewById(R.id.txtName);
        type = findViewById(R.id.txtType);
        priority = findViewById(R.id.txtPriority);
        notes = findViewById(R.id.notes);
        date = findViewById(R.id.txtDate);
        time = findViewById(R.id.txtTime);
        edit = findViewById(R.id.btn_edit);
        title = findViewById(R.id.title);

        name.setText(mTitle);
        type.setText(mType);
        date.setText(mDate);
        time.setText(mTime);
        priority.setText(mPriority);
        notes.setText(mNotes);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppointmentDetailsActivity.this, EditAppointmentActivity.class);
                intent.putExtra("name", mTitle);
                intent.putExtra("type", mType);
                intent.putExtra("date", mDate + " " + mTime);
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
                finish();
            }
        });
    }
}