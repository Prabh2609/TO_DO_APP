package com.saviour.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
    private GestureDetectorCompat gestureObject;
    private final int ADD_NEW_TASK_REQ_CODE = 100;
    private RecyclerView recyclerView;
    private TaskViewModel viewModel;
    private ItemTouchHelper itemTouchHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        dayTextView = findViewById(R.id.day);
        btnAddNewTask = findViewById(R.id.add_new_task);
        currentDay = new SimpleDateFormat("EEEE , dd/MMMM/yyyy");
        recyclerView = findViewById(R.id.recycler_view);
        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(TaskViewModel.class);
        View contextView = findViewById(R.id.item_card);

        final TaskListAdapter adapter = new TaskListAdapter(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        gestureObject = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() > e2.getX() && Math.abs(e1.getX() - e2.getX()) > 100) {
                    Intent intent = new Intent(context, DueTasks.class);
                    startActivity(intent);
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Task task = adapter.getTaskPosition(position);
                Snackbar.make(contextView, "Deleting " + task.getTitle(), Snackbar.LENGTH_SHORT).show();

                viewModel.deleteTask(task);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
            task.setTitle(data.getStringExtra("title"));
            task.setDueBy(data.getStringExtra("due_by"));
            task.setNotifyOn(data.getStringExtra("notify_on"));
            viewModel.insert(task);
        } else
            Toast.makeText(context, "CANCELLED BY USER", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return this.gestureObject.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return gestureObject.onTouchEvent(ev);
    }
}