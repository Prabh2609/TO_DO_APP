package com.saviour.todoapp;

import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.saviour.todoapp.dbUtils.Task;
import com.saviour.todoapp.dbUtils.TaskViewModel;

import java.util.List;

public class DueTasks extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private TaskViewModel viewModel;
    private GestureDetectorCompat gestureObject;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_tasks);

        context = this;
        View contextView = findViewById(R.id.item_card);
        recyclerView = findViewById(R.id.due_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        final TaskListAdapter adapter = new TaskListAdapter(context);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(TaskViewModel.class);
        viewModel.getDueTasks().observe((LifecycleOwner) context, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setTaskList(tasks);
            }
        });
        gestureObject = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getX() > e1.getX() && Math.abs(e2.getX() - e1.getX()) > 100) {
                    finish();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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