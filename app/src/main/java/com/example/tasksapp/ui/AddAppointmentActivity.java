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
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tasksapp.R;
import com.example.tasksapp.SharedPreferencesUtilities;
import com.example.tasksapp.models.Appointment;
import com.example.tasksapp.service.NotificationUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddAppointmentActivity extends AppCompatActivity {

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
    private String timestamppick;
    private StringBuilder dateinfo;
    private StringBuilder datenoticeinfo;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mDoneButton;

    private Boolean mNotified = false;

    private String mPriority;

    Appointment appointment;
    List<Appointment> moduelList;
    List<Appointment> moduelList1;
    EditText name, type, notes;
    TextView ExDate, tvTimeDateTitle, tvNoticeTitle, add_title, tvNoticeTimeDate;
    LinearLayout dateLayout, noticeLayout;
    Button save, cancel;
    ImageView alert;
    RadioGroup radioGroup;

    SharedPreferencesUtilities sharedPreferences;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        sharedPreferences = new SharedPreferencesUtilities(this);

        appointment = new Appointment();

        moduelList = new ArrayList<>();
        moduelList.clear();

        if (sharedPreferences.getAPPOINTMENTS() != null) {
            moduelList = sharedPreferences.getAPPOINTMENTS();
        } else moduelList = new ArrayList<>();

        moduelList1 = new ArrayList<>();

        name = findViewById(R.id.et_task_title);
        type = findViewById(R.id.et_task_type);
//        priority = findViewById(R.id.et_task_priority);
        notes = findViewById(R.id.et_task_content);
        ExDate = findViewById(R.id.tvTimeDate);
        tvNoticeTimeDate = findViewById(R.id.tvNoticeTimeDate);
        tvTimeDateTitle = findViewById(R.id.tv_task_date);
        tvNoticeTitle = findViewById(R.id.tv_notice_date);
        save = findViewById(R.id.btn_save);
        dateLayout = findViewById(R.id.dateLayout);
        noticeLayout = findViewById(R.id.noticeLayout);
        cancel = findViewById(R.id.btn_cancel);
        add_title = findViewById(R.id.add_title);
        radioGroup = findViewById(R.id.radioGroup);
        alert = findViewById(R.id.alert);

        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddAppointmentActivity.this, AlertActivity.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddAppointmentActivity.this, MainActivity.class));
                finish();
            }
        });

        add_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddAppointmentActivity.this, MainActivity.class));
                finish();
            }
        });

        tvTimeDateTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(AddAppointmentActivity.this);
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
                                AddAppointmentActivity.this, new mDateSetListener(), mYear, mMonth, mDay);
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
                                AddAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                                tvTimeDateTitle.setVisibility(View.GONE);

                                Log.e("AddTask Date >> ", datepick + " " + timepick);

                                dateLayout.setVisibility(View.VISIBLE);
                                ExDate.setText(datepick + " " + timepick);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(AddAppointmentActivity.this, "Please Select Date and Time", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(AddAppointmentActivity.this);
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
                                AddAppointmentActivity.this, new mDateSetListener(), mYear, mMonth, mDay);
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
                                AddAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                                tvTimeDateTitle.setVisibility(View.GONE);

                                Log.e("AddTask Date >> ", datepick + " " + timepick);

                                dateLayout.setVisibility(View.VISIBLE);
                                ExDate.setText(datepick + " " + timepick);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(AddAppointmentActivity.this, "Please Select Date and Time", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        tvNoticeTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateinfo != null) {
                    Dialog dialog = new Dialog(AddAppointmentActivity.this);
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
                                    AddAppointmentActivity.this, new mNoticeDateSetListener(), mYear, mMonth, mDay);
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
                                    AddAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                                    timenoticeinfo = mHour + ":" + mMinute + ":" + "00";
                                    mTimeButton.setText(timenoticeinfo);

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
                                longNoticeDate = date.getTime();

                                str = sdf.format(date);
                                if (str != null) {
                                    String[] arr = str.split(" ");

                                    StringBuilder st = new StringBuilder();

                                    String txt = arr[0] + " " + arr[1];
                                    st.append(txt);
                                    Log.e("AddTask >> ", st.toString());
                                    tvNoticeTitle.setVisibility(View.GONE);
                                    noticeLayout.setVisibility(View.VISIBLE);

                                    Log.e("AddTask Notice Date >> ", String.format(Locale.getDefault(), st.toString()));

//                                    tvNoticeTimeDate.setText(String.format(Locale.getDefault(), st.toString()));
                                    tvNoticeTimeDate.setText(datepick + " " + timepick);
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(AddAppointmentActivity.this, "Please Select Date and Time", Toast.LENGTH_SHORT).show();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    Toast.makeText(AddAppointmentActivity.this, "Please choose Task Time first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        noticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateinfo != null) {
                    Dialog dialog = new Dialog(AddAppointmentActivity.this);
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
                                    AddAppointmentActivity.this, new mNoticeDateSetListener(), mYear, mMonth, mDay);
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
                                    AddAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                                    timenoticeinfo = mHour + ":" + mMinute + ":" + "00";
                                    mTimeButton.setText(timenoticeinfo);

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
                                longNoticeDate = date.getTime();

                                str = sdf.format(date);
                                if (str != null) {
                                    String[] arr = str.split(" ");

                                    StringBuilder st = new StringBuilder();

                                    String txt = arr[0] + " " + arr[1];
                                    st.append(txt);
                                    Log.e("AddTask >> ", st.toString());
                                    tvNoticeTitle.setVisibility(View.GONE);
                                    noticeLayout.setVisibility(View.VISIBLE);

                                    Log.e("AddTask Notice Date >> ", String.format(Locale.getDefault(), st.toString()));

//                                    tvNoticeTimeDate.setText(String.format(Locale.getDefault(), st.toString()));
                                    tvNoticeTimeDate.setText(datepick + " " + timepick);
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(AddAppointmentActivity.this, "Please Select Date and Time", Toast.LENGTH_SHORT).show();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    Toast.makeText(AddAppointmentActivity.this, "Please choose Task Time first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.high:
                        mPriority = "high";
                        break;

                    case R.id.medium:
                        mPriority = "medium";
                        break;

                    case R.id.low:
                        mPriority = "low";
                        break;
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = name.getText().toString();
                String mType = type.getText().toString();
//                String mPriority = priority.getText().toString();
                String content = notes.getText().toString();

                if (longNoticeDate != 0) {

                    if (!mNotified)
                        new NotificationUtils().setNotification(longNoticeDate, AddAppointmentActivity.this);

                    Log.e("AddActivity", "title >> " + title + " type >> " + mType + " priority >> " + mPriority + " notes >> " + content
                            + " time >> " + timeinfo + " date >> " + dateinfo + " " + "notice time >> " + timenoticeinfo
                            + " " + "notice date >> " + datenoticeinfo + " longDate >> " + longdate);

                    appointment = new Appointment(title, mType, dateinfo.toString(), datenoticeinfo.toString(),
                            timeinfo, timenoticeinfo, longNoticeDate, mPriority, content);

                    moduelList1.clear();
                    moduelList.add(appointment);
                    moduelList1.addAll(moduelList);

                    sharedPreferences.setAPPOINTMENTS(moduelList1);

                    Toast.makeText(AddAppointmentActivity.this, "Appointment is Saved", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddAppointmentActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(AddAppointmentActivity.this, "Please complete required data", Toast.LENGTH_SHORT).show();
                }
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
                month = (mMonth + 1) + "";
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
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND,0);
        return (int) (c.getTimeInMillis() / 1000L);
    }
}



