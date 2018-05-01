package com.ciastkaipiwo.android.scrummajster;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SprintActivity extends AppCompatActivity {

    private static final String PROJECT_ID = "com.ciastkaipiwo.android.scrummajster.project_id";
    private static final String SPRINT = "com.ciastkaipiwo.android.scrummajster.sprint";

    private int mProjectId;
    private Sprint mSprint;
    private TextView mSprintStartDate;
    private TextView mSprintEndDate;
    private ProjectsDBHelper mDatabaseHelper;

    private List<Task> mTasksList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TasksAdapter mTasksAdapter;

    @Override
    public void onResume() {
        super.onResume();
        initTasksData();
        mRecyclerView.setAdapter(mTasksAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint);

        mDatabaseHelper = new ProjectsDBHelper(this);
        mProjectId = getIntent().getIntExtra(PROJECT_ID,-1);
        mSprint = getIntent().getParcelableExtra(SPRINT);
        mSprintStartDate = (TextView) findViewById(R.id.sprint_start_date);
        mSprintEndDate = (TextView) findViewById(R.id.sprint_end_date);

        mRecyclerView = (RecyclerView) findViewById(R.id.sprint_recycler_view);
        mTasksAdapter = new TasksAdapter(mTasksList, mProjectId);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        initTasksData();
        mRecyclerView.setAdapter(mTasksAdapter);

        mSprintStartDate.setText(mSprint.getStartDate().getTime().toString());
        mSprintEndDate.setText(mSprint.getEndDate().getTime().toString());

    }


    public void initTasksData() {
        mTasksList.clear();
        Cursor data = mDatabaseHelper.getSprintTasks(mProjectId,mSprint.getId());
        while (data.moveToNext()) {
            int id = data.getInt(0);
            String story = data.getString(3);
            int weight = data.getInt(4);
            int time = data.getInt(5);
            mTasksList.add(new Task(id,story, weight, time));
        }
    }

}
