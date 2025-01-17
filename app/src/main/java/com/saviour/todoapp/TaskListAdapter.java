package com.saviour.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saviour.todoapp.dbUtils.Task;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    private List<Task> taskList;
    final LayoutInflater inflater;

    TaskListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if (taskList != null) {
            Task task = taskList.get(position);
            holder.listItemView.setText(task.getTitle());
            holder.dueBY.setText("Due By : " + task.getDueBy());
            holder.notifyOn.setText("Notify On : " + task.getNotifyOn());
        } else {
            holder.listItemView.setText("Nothing :(");
        }
    }

    @Override
    public int getItemCount() {
        if (taskList != null) {
            return taskList.size();
        } else {
            return 0;
        }
    }

    public void setTaskList(List<Task> tasks) {
        taskList = tasks;
        notifyDataSetChanged();
    }

    public Task getTaskPosition(int position) {
        return taskList.get(position);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        final TextView listItemView, dueBY, notifyOn;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            listItemView = itemView.findViewById(R.id.text_view);
            dueBY = itemView.findViewById(R.id.due_text_view);
            notifyOn = itemView.findViewById(R.id.notify_text_view);
        }
    }
}