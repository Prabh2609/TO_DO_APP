package com.saviour.todoapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Add_New_Task extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.saviour.todoapp.EXTRA_REPLY";
    private TextInputEditText task_title;
    private MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__task);

        task_title = findViewById(R.id.text_task_title);
        btnSave = findViewById(R.id.btn_save);


    }
}
