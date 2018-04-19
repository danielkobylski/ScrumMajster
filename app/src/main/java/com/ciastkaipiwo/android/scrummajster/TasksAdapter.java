package com.ciastkaipiwo.android.scrummajster;

/**
 * Created by Klaudia on 12.04.2018.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder>{

    private List<Task> tasksList;

    public TasksAdapter(List<Task> tasksList) {
        this.tasksList = tasksList;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasks_list_row, parent, false);
        return new TaskViewHolder(itemView, tasksList);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        holder.name.setText(tasksList.get(position).getStory());
        holder.weight.setText("Weight: "+String.valueOf(tasksList.get(position).getWeight()));
        holder.time.setText("Time: "+String.valueOf(tasksList.get(position).getTime()));
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;
        public TextView weight;
        public TextView time;
        public int position;
        //private ArrayList<Task> taskList;


        public TaskViewHolder(View view, List<Task>taskList) {
            super(view);
            view.setOnClickListener(this);
            //this.taskList = (ArrayList<Task>) taskList;
            name = (TextView) view.findViewById(R.id.task_row_story);
            weight = (TextView) view.findViewById(R.id.task_row_weight);
            time = (TextView) view.findViewById(R.id.task_row_time);
        }
        @Override
        public void onClick(View v){
            Intent intent = TaskActivity.newIntent(v.getContext(), tasksList.get(this.position));
            //intent.putExtra("task",this.taskList.get(this.position));
            //intent.putExtra("position",position);
            v.getContext().startActivity(intent);
        }
    }

}