package com.saviour.todoapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.saviour.todoapp.dbUtils.Task;
import com.saviour.todoapp.dbUtils.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 0;
    private TextView dayTextView;
    private String PRIMARY_CHANEL_ID = "primary_notification_channel";
    private NotificationManager mNotificationManager;
    private SimpleDateFormat currentDay;
    private FloatingActionButton btnAddNewTask;
    private Context context;
    private GestureDetectorCompat gestureObject;
    private final int ADD_NEW_TASK_REQ_CODE = 100;
    private RecyclerView recyclerView;
    private TaskViewModel viewModel;
    private ItemTouchHelper itemTouchHelper;
    private TaskListAdapter adapter;
    private Calendar myCalendar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        dayTextView = findViewById(R.id.day);
        btnAddNewTask = findViewById(R.id.add_new_task);
        currentDay = new SimpleDateFormat("EEEE , dd/MMMM/yyyy");
        recyclerView = findViewById(R.id.recycler_view);
        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(TaskViewModel.class);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        myCalendar = Calendar.getInstance();

        adapter = new TaskListAdapter(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        gestureObject = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() > e2.getX() && Math.abs(e1.getX() - e2.getX()) > 100) {
                    Intent intent = new Intent(context, DueTasks.class);
                    startActivity(intent);
                } else if (e1.getY() > e2.getY() && Math.abs(e1.getY() - getScreenHeight()) < 100) {
                    Intent intent = new Intent(context, Add_New_Task.class);
                    startActivityForResult(intent, ADD_NEW_TASK_REQ_CODE);
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
                Toast.makeText(context, "Deleting " + task.getTitle(), Toast.LENGTH_SHORT).show();

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

        createNotificationChannel();
    }

    public void createNotificationChannel() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANEL_ID, "Alarm Notification", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setDescription("Alarm for every to do task");

            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NEW_TASK_REQ_CODE && resultCode == RESULT_OK) {
            Task task = new Task();
            task.setTitle(data.getStringExtra("title"));
            task.setDueBy(data.getStringExtra("due_by"));
            task.setNotifyOn(data.getStringExtra("notify_on"));

            Intent notifyIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(context, task.getTid(), notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            setAlarm(notifyPendingIntent, data.getStringExtra("notify_on"));

            viewModel.insert(task);

        } else
            Toast.makeText(context, "CANCELLED BY USER", Toast.LENGTH_LONG).show();
    }

    private void setAlarm(PendingIntent notifyPendingIntent, String notify_on) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        String DateTime = notify_on;
        String Date = DateTime.split(" ")[0];
        String Time = DateTime.split(" ")[1];

        myCalendar.setTimeInMillis(System.currentTimeMillis());
        myCalendar.set(Calendar.YEAR, Integer.parseInt(Date.split("/")[2]));
        myCalendar.set(Calendar.MONTH, Integer.parseInt(Date.split("/")[1]) - 1);
        myCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(Date.split("/")[0]));
        myCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Time.split(":")[0]));
        myCalendar.set(Calendar.MINUTE, Integer.parseInt(Time.split(":")[1]));

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(), notifyPendingIntent);
            }
        }
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

    private int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.heightPixels;
    }
}