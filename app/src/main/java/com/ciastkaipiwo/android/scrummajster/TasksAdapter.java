package com.ciastkaipiwo.android.scrummajster;

/**
 * Created by Klaudia on 12.04.2018.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder>{

    private List<Task> tasksList;
    private int mProjectId;


    public TasksAdapter(List<Task> tasksList, int projectId) {
        this.tasksList = tasksList;
        mProjectId = projectId;
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

        private static final int REQUEST_CODE_EDIT_TASK = 2;
        private static final int REQUEST_CODE_ADD_TO_SPRINT = 4;

        public TextView name;
        public TextView weight;
        public TextView time;
        public int position;
        public ImageButton mTaskMenu;
        public ProjectsDBHelper mDatabaseHelper;

        public TaskViewHolder(final View view, List<Task>taskList) {
            super(view);
            view.setOnClickListener(this);
            //this.taskList = (ArrayList<Task>) taskList;
            name = (TextView) view.findViewById(R.id.task_row_story);
            weight = (TextView) view.findViewById(R.id.task_row_weight);
            time = (TextView) view.findViewById(R.id.task_row_time);
            mDatabaseHelper = new ProjectsDBHelper(view.getContext());

            mTaskMenu = (ImageButton) view.findViewById(R.id.task_menu);
            final PopupMenu pum = new PopupMenu(view.getContext(),mTaskMenu);

            String currentActivity = view.getContext().getClass().getName();
            System.out.println(currentActivity);
            if (currentActivity.equals("com.ciastkaipiwo.android.scrummajster.BacklogActivity")) {
                pum.inflate(R.menu.backlog_task_menu);
            } else {
                pum.inflate(R.menu.sprint_task_menu);
            }

            pum.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.task_edit_button: {
                            Intent intent = TaskConfigActivity.newIntent(view.getContext(),tasksList.get(position));
                            ((Activity) view.getContext()).startActivityForResult(intent, REQUEST_CODE_EDIT_TASK);
                            break;
                        }
                        case R.id.task_delete_button: {
                            mDatabaseHelper.removeTask(tasksList.get(position));
                            Intent tempIntent = new Intent(view.getContext(), view.getContext().getClass());
                            tempIntent.putExtra("refresher", 1);
                            view.getContext().startActivity(tempIntent);
                            break;
                        }
                        case R.id.task_add_to_sprint_button: {
                            //Intent intent = new Intent(view.getContext(), SprintChoiceActivity.class);
                            //((Activity) view.getContext()).startActivityForResult(intent, REQUEST_CODE_ADD_TO_SPRINT);
                           mDatabaseHelper.moveTask(mProjectId,tasksList.get(position),getActiveSprint().getId());
                           System.out.println(view.getContext()+" " + view.getContext().getClass());
                           Intent tempIntent = new Intent(view.getContext(), view.getContext().getClass());
                           tempIntent.putExtra("refresher", 1);
                           view.getContext().startActivity(tempIntent);
                            break;
                        }

                        case R.id.task_add_to_backlog_button: {
                            mDatabaseHelper.moveTask(mProjectId,tasksList.get(position),-1);
                            System.out.println(view.getContext()+" " + view.getContext().getClass());
                            Intent tempIntent = new Intent(view.getContext(), view.getContext().getClass());
                            tempIntent.putExtra("refresher", 1);
                            view.getContext().startActivity(tempIntent);
                            break;
                        }
                    }
                    return true;
                }
            });

            mTaskMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pum.show();
                }
            });

        }
        //===============================================================////
        //// FUNKCJA DO USUNIECIA PO ZAIMPLEMENTOWANIU SPRINT ADAPTERA   ////
        //===============================================================////
        private Sprint getActiveSprint() {
            Cursor data = mDatabaseHelper.getSprints(mProjectId);
            ArrayList<Sprint> sprints = new ArrayList<Sprint>();
            long today = System.currentTimeMillis();
            while (data.moveToNext()) {
                long startDate = data.getLong(2);
                long endDate = data.getLong(3);
                if (today >= startDate && today <= endDate) {
                    int id = data.getInt(0);
                    GregorianCalendar gStartDate = new GregorianCalendar();
                    GregorianCalendar gEndDate = new GregorianCalendar();
                    gStartDate.setTimeInMillis(startDate);
                    gEndDate.setTimeInMillis(endDate);
                    return new Sprint(id, gStartDate,gEndDate);
                }
            }
            return null;
        }


        @Override
        public void onClick(View v){
            if(!String.valueOf(v.getContext().getClass()).equals("class com.ciastkaipiwo.android.scrummajster.BacklogActivity")) {
                Intent intent = KanbanActivity.newIntent(v.getContext(), tasksList.get(this.position));
                v.getContext().startActivity(intent);
            }
        }
    }


    /*
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

*/
}