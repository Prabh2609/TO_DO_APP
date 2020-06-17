package com.saviour.todoapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Add_New_Task extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.saviour.todoapp.EXTRA_REPLY";
    private TextInputEditText task_title, due_by, notify_on;
    private MaterialButton btnSave;
    private MaterialToolbar toolbar;
    private Context context;
    private Calendar myCalendar;
    private Intent replyIntent = new Intent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__task);

        task_title = findViewById(R.id.text_task_title);
        due_by = findViewById(R.id.text_due_by);
        notify_on = findViewById(R.id.text_notify_on);
        btnSave = findViewById(R.id.btn_save);
        toolbar = findViewById(R.id.toolbar);
        myCalendar = Calendar.getInstance();
        context = this;

        due_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(due_by);
            }
        });
        notify_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(notify_on);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, replyIntent);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(task_title.getText())) {
                    task_title.setError("Please Fill out this field");
                } else if (TextUtils.isEmpty(due_by.getText())) {
                    task_title.setError("Please Fill out this field");
                } else if (TextUtils.isEmpty(notify_on.getText())) {
                    task_title.setError("Please Fill out this field");
                } else {
                    replyIntent.putExtra("title", task_title.getText().toString());
                    replyIntent.putExtra("due_by", due_by.getText().toString());
                    replyIntent.putExtra("notify_on", notify_on.getText().toString());
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            }
        });

    }

    private void showDateTimePicker(final TextInputEditText textInputEditText) {
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCalendar.set(Calendar.MINUTE, minute);

                        textInputEditText.setText(simpleDateFormat.format(myCalendar.getTime()) + " " + timeFormat.format(myCalendar.getTime()));
                    }
                }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
