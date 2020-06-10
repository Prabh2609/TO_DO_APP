package com.saviour.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saviour.todoapp.dbUtils.Task;
import com.saviour.todoapp.dbUtils.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView dayTextView;
    private SimpleDateFormat currentDay;
    private FloatingActionButton btnAddNewTask;
    private Context context;
    private final int ADD_NEW_TASK_REQ_CODE = 100;
    private RecyclerView recyclerView;
    private TaskViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        dayTextView = findViewById(R.id.day);
        btnAddNewTask = findViewById(R.id.add_new_task);
        currentDay = new SimpleDateFormat("EEEE , dd/MMMM/yyyy");
        recyclerView = findViewById(R.id.recycler_view);
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        final TaskListAdapter adapter = new TaskListAdapter(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        dayTextView.setText(new StringBuilder().append(currentDay.format(new Date())));

        viewModel.getAllTasks().observe((LifecycleOwner) context, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setTaskList(tasks);
            }
        });

        btnAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Add_New_Task.class);
                startActivityForResult(intent, ADD_NEW_TASK_REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NEW_TASK_REQ_CODE && resultCode == RESULT_OK) {
            Task task = new Task();
            task.setTitle(data.getStringExtra(Add_New_Task.EXTRA_REPLY));
            viewModel.insert(task);
        } else
            Toast.makeText(context, "CANCELLED BY USER", Toast.LENGTH_LONG).show();
    }
}
