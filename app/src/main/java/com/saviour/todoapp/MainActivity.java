package com.saviour.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView dayTextView;
    private SimpleDateFormat currentDay;
    private FloatingActionButton btnAddNewTask;
    private Context context;
    private ArrayList<String> toDoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        variableInitializer();
        updateDay();

        btnAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNewTask.display(getSupportFragmentManager());
            }
        });
    }

    private void variableInitializer(){
        context = this;
        dayTextView = findViewById(R.id.day);
        btnAddNewTask = findViewById(R.id.add_new_task);
        currentDay = new SimpleDateFormat("EEEE , dd/MMMM/yyyy");
    }
    private void updateDay(){
        dayTextView.setText(
                new StringBuilder()
                .append(currentDay.format(new Date()))
        );
    }

}
