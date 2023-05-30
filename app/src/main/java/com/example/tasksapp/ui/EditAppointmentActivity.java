package com.example.tasksapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tasksapp.R;
import com.example.tasksapp.SharedPreferencesUtilities;
import com.example.tasksapp.models.Appointment;
import com.example.tasksapp.service.NotificationUtils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditAppointmentActivity extends AppCompatActivity {

    private Calendar c = Calendar.getInstance();
    private int mYear;
    private int mMonth;
    private int mDay;
    private int hour;
    private int minute;
    private int second;
    private long time;
    private long date;
    private long longdate;
    private long longNoticeDate;
    private String timeinfo;
    private String timenoticeinfo;
    private String str;
    private String s;
    private String timepick;
    private String datepick;
    private String noticetimepick;
    private String noticedatepick;
    private String timestamppick;
    private StringBuilder dateinfo;
    private StringBuilder datenoticeinfo;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mDoneButton;

    private Boolean mNotified = false;

    private String priority;

    Appointment appointment;
    List<Appointment> moduelList;
    List<Appointment> moduelList1;
    EditText name, type, notes;
    TextView ExDate, tvNoticeTimeDate, add_title;
    LinearLayout dateLayout, noticeLayout;
    Button save, cancel;
    RadioGroup radioGroup;
    RadioButton high, medium, low;

    SharedPreferencesUtilities sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);

        sharedPreferences = new SharedPreferencesUtilities(this);

        appointment = new Appointment();

        Intent intent = getIntent();
        String title = intent.getStringExtra("name");
        String mType = intent.getStringExtra("type");
        String mDateTime = intent.getStringExtra("date");
        String mNoticeDateTime = intent.getStringExtra("noticeDate");
        String mNotes = intent.getStringExtra("notes");
        String mPriority = intent.getStringExtra("priority");
        int index = intent.getIntExtra("index", 0);

        Log.e("EditAppointmentActivity", "Appointment >> " + intent.getStringExtra("priority"));

        if (sharedPreferences.getAPPOINTMENTS() != null)
            moduelList = sharedPreferences.getAPPOINTMENTS();
        else moduelList = new ArrayList<>();

        moduelList1 = new ArrayList<>();

        name = findViewById(R.id.et_task_name);
        type = findViewById(R.id.et_task_mType);
        notes = findViewById(R.id.et_task_content);
        ExDate = findViewById(R.id.tvTimeDate);
        tvNoticeTimeDate = findViewById(R.id.tvNoticeTimeDate);
        dateLayout = findViewById(R.id.dateLayout);
        noticeLayout = findViewById(R.id.noticeLayout);
        save = findViewById(R.id.btn_edit);
        cancel = findViewById(R.id.btn_close);
        add_title = findViewById(R.id.add_title);
        radioGroup = findViewById(R.id.radioGroup);
        high = findViewById(R.id.high);
        medium = findViewById(R.id.medium);
        low = findViewById(R.id.low);

        name.setText(title);
        type.setText(mType);
        ExDate.setText(mDateTime);
        tvNoticeTimeDate.setText(mNoticeDateTime);
        notes.setText(mNotes);

        if (mPriority.equals("high")){
            priority = "high";
            high.setChecked(true);
        }else if (mPriority.equals("medium")){
            priority = "medium";
            medium.setChecked(true);
        }else {
            priority = "low";
            low.setChecked(true);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditAppointmentActivity.this, MainActivity.class));
                finish();
            }
        });

        add_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditAppointmentActivity.this, MainActivity.class));
                finish();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.high:
                        priority = "high";
                        break;

                    case R.id.medium:
                        priority = "medium";
                        break;

                    case R.id.low:
                        priority = "low";
                        break;
                }
            }
        });

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(EditAppointmentActivity.this);
                dialog.setContentView(R.layout.date_dialog);
                dialog.setCancelable(true);
                dialog.show();

                mDateButton = dialog.findViewById(R.id.btn_date);
                mTimeButton = dialog.findViewById(R.id.btn_time);
                mDoneButton = dialog.findViewById(R.id.btn_done);

                mDateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                        date = c.getTimeInMillis() / 1000L;
                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                EditAppointmentActivity.this, new mDateSetListener(), mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                });
                mTimeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hour = c.get(Calendar.HOUR_OF_DAY);
                        minute = c.get(Calendar.MINUTE);
                        second = c.get(Calendar.SECOND);
                        TimePickerDialog mTimePicker = new TimePickerDialog(
                                EditAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                hour = selectedHour;
                                minute = selectedMinute;
                                String mHour;
                                String mMinute;
                                if (selectedHour <= 9) {
                                    mHour = "0" + selectedHour;
                                } else {
                                    mHour = selectedHour + "";
                                }

                                if (selectedMinute <= 9) {
                                    mMinute = "0" + selectedMinute;
                                } else {
                                    mMinute = selectedMinute + "";
                                }
                                timeinfo = mHour + ":" + mMinute + ":" + "00";
                                mTimeButton.setText(timeinfo);

                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR, hour);
                                c.set(Calendar.MINUTE, minute);
                                c.set(Calendar.SECOND, second);
                                time = c.getTimeInMillis() / 1000L;
                            }

                        }, hour, minute, true);

                        // Yes 24 hour time
                        mTimePicker.setTitle("Select  Time");
                        mTimePicker.show();

                        timestamp(mYear, mMonth, mDay, hour, minute);

                    }
                });

                mDoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c = Calendar.getInstance();
                        c.set(Calendar.MONTH, mMonth);
                        c.set(Calendar.YEAR, mYear);
                        c.set(Calendar.DAY_OF_MONTH, mDay);

                        c.set(Calendar.HOUR_OF_DAY, hour);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, second);

                        long result1 = c.getTimeInMillis() / 1000L;
                        timepick = mTimeButton.getText().toString();
                        datepick = mDateButton.getText().toString();
                        timestamppick = Long.toString(result1);

                        s = datepick + " " + timepick;

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date date = sdf.parse(s);
                            longdate = date.getTime();

                            str = sdf.format(date);
                            if (str != null) {
                                String[] arr = str.split(" ");

                                StringBuilder st = new StringBuilder();

                                String txt = arr[0] + " " + arr[1];
                                st.append(txt);
                                Log.e("AddTask >> ", st.toString());
                                ExDate.setText(datepick + " " + timepick);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(EditAppointmentActivity.this, "Please Select Date and Time", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        noticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(EditAppointmentActivity.this);
                dialog.setContentView(R.layout.date_dialog);
                dialog.setCancelable(true);
                dialog.show();

                mDateButton = dialog.findViewById(R.id.btn_date);
                mTimeButton = dialog.findViewById(R.id.btn_time);
                mDoneButton = dialog.findViewById(R.id.btn_done);

                mDateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                        date = c.getTimeInMillis() / 1000L;
                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                EditAppointmentActivity.this, new mNoticeDateSetListener(), mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                });
                mTimeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hour = c.get(Calendar.HOUR_OF_DAY);
                        minute = c.get(Calendar.MINUTE);
                        second = c.get(Calendar.SECOND);
                        TimePickerDialog mTimePicker = new TimePickerDialog(
                                EditAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                hour = selectedHour;
                                minute = selectedMinute;
                                String mHour;
                                String mMinute;
                                if (selectedHour <= 9) {
                                    mHour = "0" + selectedHour;
                                } else {
                                    mHour = selectedHour + "";
                                }

                                if (selectedMinute <= 9) {
                                    mMinute = "0" + selectedMinute;
                                } else {
                                    mMinute = selectedMinute + "";
                                }
                                timeinfo = mHour + ":" + mMinute + ":" + "00";
                                mTimeButton.setText(timeinfo);

                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR, hour);
                                c.set(Calendar.MINUTE, minute);
                                c.set(Calendar.SECOND, second);
                                time = c.getTimeInMillis() / 1000L;
                            }

                        }, hour, minute, true);

                        // Yes 24 hour time
                        mTimePicker.setTitle("Select  Time");
                        mTimePicker.show();

                        timestamp(mYear, mMonth, mDay, hour, minute);

                    }
                });

                mDoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c = Calendar.getInstance();
                        c.set(Calendar.MONTH, mMonth);
                        c.set(Calendar.YEAR, mYear);
                        c.set(Calendar.DAY_OF_MONTH, mDay);

                        c.set(Calendar.HOUR_OF_DAY, hour);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, second);

                        long result1 = c.getTimeInMillis() / 1000L;
                        noticetimepick = mTimeButton.getText().toString();
                        noticedatepick = mDateButton.getText().toString();
                        timestamppick = Long.toString(result1);

                        s = noticedatepick + " " + noticetimepick;

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date date = sdf.parse(s);
                            longNoticeDate = date.getTime();

                            str = sdf.format(date);
                            if (str != null) {
                                String[] arr = str.split(" ");

                                StringBuilder st = new StringBuilder();

                                String txt = arr[0] + " " + arr[1];
                                st.append(txt);
                                Log.e("AddTask >> ", st.toString());
                                tvNoticeTimeDate.setText(noticedatepick + " " + noticetimepick);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(EditAppointmentActivity.this, "Please Select Date and Time", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = name.getText().toString();
                String mType = type.getText().toString();
//                int mPriority = 1;
                String content = notes.getText().toString();
                String date = tvNoticeTimeDate.getText().toString();
                String noticeDate = ExDate.getText().toString();

                String[] arrDate = date.split(" ");

                long result1 = c.getTimeInMillis() / 1000L;
                noticetimepick = arrDate[1];
                timenoticeinfo = noticetimepick;
                noticedatepick = arrDate[0];
                timestamppick = Long.toString(result1);

                s = noticedatepick + " " + noticetimepick;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date1 = sdf.parse(s);
                    longNoticeDate = date1.getTime();

                    str = sdf.format(date1);
                    if (str != null) {
                        String[] arr = str.split(" ");

                        StringBuilder st = new StringBuilder();

                        String txt = arr[0] + " " + arr[1];

                        Log.e("EditActivity", "Text Date >> "+txt);
                        st.append(txt);
                        Log.e("AddTask >> ", st.toString());
                        tvNoticeTimeDate.setText(noticedatepick + " " + noticetimepick);

                    } else {
                        Toast.makeText(EditAppointmentActivity.this, "Please Select Notice Date and Time", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String[] arrDate2 = noticeDate.split(" ");

                long result2 = c.getTimeInMillis() / 1000L;
                timepick = arrDate2[1];
                timeinfo = timepick;
                datepick = arrDate2[0];
                timestamppick = Long.toString(result2);

                s = datepick + " " + timepick;

                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date2 = sdf1.parse(s);
                    longNoticeDate = date2.getTime();

                    str = sdf.format(date2);
                    if (str != null) {
                        String[] arr = str.split(" ");

                        StringBuilder st = new StringBuilder();

                        String txt = arr[0] + " " + arr[1];

                        Log.e("EditActivity", "Text Date >> "+txt);
                        st.append(txt);
                        Log.e("AddTask >> ", st.toString());
                        ExDate.setText(datepick + " " + timepick);

                    } else {
                        Toast.makeText(EditAppointmentActivity.this, "Please Select Date and Time", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (!mNotified)
                    new NotificationUtils().setNotification(longNoticeDate, EditAppointmentActivity.this);

                Log.e("AddActivity", "title >> " + title + " type >> " + mType + " priority >> " + mPriority + " notes >> " + content
                        + " time >> " + timeinfo + " date >> " + datepick + " longDate >> " + longNoticeDate);

                appointment = new Appointment(title, mType, datepick, noticedatepick, timepick, noticetimepick, longdate, priority, content);
                moduelList.set(index,appointment);

                moduelList1.addAll(moduelList);

                sharedPreferences.setAPPOINTMENTS(moduelList1);

                Toast.makeText(EditAppointmentActivity.this, "Appointment is Saved", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditAppointmentActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    final class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        public void onDateSet(@NotNull DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, mYear);
            c.set(Calendar.MONTH, mMonth);
            c.set(Calendar.DAY_OF_MONTH, mDay);
            String month;
            String day;
            if (mMonth <= 9) {
                month = "0" + (mMonth + 1);
            } else {
                month = (mMonth + 1) + "";
            }
            if (mDay <= 9) {
                day = "0" + mDay;
            } else {
                day = mDay + "";
            }
            dateinfo = new StringBuilder().append(mYear).append("-").append(month).append("-").append(day);
            mDateButton.setText(dateinfo.toString());
        }
    }

    final class mNoticeDateSetListener implements DatePickerDialog.OnDateSetListener {
        public void onDateSet(@NotNull DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, mYear);
            c.set(Calendar.MONTH, mMonth);
            c.set(Calendar.DAY_OF_MONTH, mDay);
            String month;
            String day;
            if (mMonth <= 9) {
                month = "0" + (mMonth + 1);
            } else {
                month = (mMonth + 1)+ "";
            }
            if (mDay <= 9) {
                day = "0" + mDay;
            } else {
                day = mDay + "";
            }
            datenoticeinfo = new StringBuilder().append(mYear).append("-").append(month).append("-").append(day);
            mDateButton.setText(datenoticeinfo.toString());
        }
    }

    private final int timestamp(int mYear, int mMonth, int mDay, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, mYear);
        c.set(Calendar.MONTH, mMonth);
        c.set(Calendar.DAY_OF_MONTH, mDay);
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(14, 0);
        return (int) (c.getTimeInMillis() / 1000L);
    }
}