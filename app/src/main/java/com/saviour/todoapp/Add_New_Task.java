package com.saviour.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Add_New_Task extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.saviour.todoapp.EXTRA_REPLY";
    private TextInputEditText task_title;
    private MaterialButton btnSave;
    private MaterialToolbar toolbar;
    private Intent replyIntent = new Intent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__task);

        task_title = findViewById(R.id.text_task_title);
        btnSave = findViewById(R.id.btn_save);
        toolbar = findViewById(R.id.toolbar);

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
                } else {
                    replyIntent.putExtra(EXTRA_REPLY, task_title.getText().toString());
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            }
        });

    }
}
