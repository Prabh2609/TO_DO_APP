package com.saviour.todoapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private TextView dayTextView;
    private SimpleDateFormat currentDay;
    private FloatingActionButton btnAddNewTask;
    private Context context;
    public static final int CREATE_NEW_TASK_REQUEST_CODE = 1;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        dayTextView = findViewById(R.id.day);
        btnAddNewTask = findViewById(R.id.add_new_task);
        currentDay = new SimpleDateFormat("EEEE , dd/MMMM/yyyy");


        dayTextView.setText(new StringBuilder().append(currentDay.format(new Date())));

        btnAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Create New Task", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
