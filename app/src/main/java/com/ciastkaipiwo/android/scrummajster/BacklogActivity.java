package com.ciastkaipiwo.android.scrummajster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import java.util.ArrayList;
import java.util.List;

public class BacklogActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_TASK = 1;
    private static final int REQUEST_CODE_EDIT_TASK = 3;
    private static final int REQUEST_CODE_ADD_TO_SPRINT = 4;

    private static final String PROJECT_ID = "com.ciastkaipiwo.android.scrummajster.project_id";
    private static final String TASK_TO_MOVE = "com.ciastkaipiwo.android.scrummajster.task_to_move";
    private static final String SPRINT_CHOSEN = "com.ciastkaipiwo.android.scrummajster.sprint_chosen";


    private FloatingActionButton mPlus;
    private ProjectsDBHelper mDatabaseHelper;

    private List<Task> mTasksList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TasksAdapter mTasksAdapter;
    private int projectId;

    public void onResume() {
        super.onResume();
        if (getIntent().getIntExtra("refresher", -1) == 1) {
            finish();
        }
        initTasksData();
        mRecyclerView.setAdapter(mTasksAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlog);
        mDatabaseHelper = new ProjectsDBHelper(this);

        projectId = getIntent().getIntExtra(PROJECT_ID, -1);
        System.out.println("BacklogActivity PROJECT ID: " + projectId);

        mRecyclerView = (RecyclerView) findViewById(R.id.backlog_recycler_view);
        mTasksAdapter = new TasksAdapter(mTasksList, projectId);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mTasksAdapter);


        mPlus = (FloatingActionButton) findViewById(R.id.add_task);
        mPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BacklogActivity.this, TaskConfigActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_TASK);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_ADD_TASK) {
            if (data == null) {
                return;
            }
            mDatabaseHelper.addTask(projectId,-1,TaskConfigActivity.getNewTask(data));
            Toast.makeText(BacklogActivity.this, "Pomyslnie dodano task: "+String.valueOf(TaskConfigActivity.getNewTask(data).getTime()), Toast.LENGTH_LONG).show();
        }
        else if (requestCode == REQUEST_CODE_EDIT_TASK) {
            if (data == null) {
                return;
            }
            mDatabaseHelper.editTask(TaskConfigActivity.getOldTask(data), (TaskConfigActivity.getNewTask(data)));
        }

        else if (requestCode == REQUEST_CODE_ADD_TO_SPRINT) {
            if (data == null) {
                return;
            }
            Sprint sprintChosen = data.getParcelableExtra(SPRINT_CHOSEN);
            Task taskToMove = data.getParcelableExtra(TASK_TO_MOVE);
            mDatabaseHelper.moveTask(projectId, taskToMove, sprintChosen.getId());
        }
        //else if (requestCode == REQUEST_CODE_ADD_TO_SPRINT) {
        //    if (data == null) {
        //        return
        //    }
        //    mDatabaseHelper.moveTask()
        //}
    }

    public static Intent newIntent(Context packageContext, Project project){
        Intent intent = new Intent(packageContext, BacklogActivity.class);
        intent.putExtra(PROJECT_ID, project.getId());
        return intent;
    }

    public void initTasksData() {
        mTasksList.clear();
        Cursor data = mDatabaseHelper.getBacklogTasks(projectId);
        while (data.moveToNext()) {
            int id = data.getInt(0);
            String story = data.getString(3);
            int weight = data.getInt(4);
            int time = data.getInt(5);
            mTasksList.add(new Task(id,story, weight, time));
        }
    }

}