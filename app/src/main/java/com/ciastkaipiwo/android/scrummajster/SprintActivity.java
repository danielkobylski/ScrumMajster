package com.ciastkaipiwo.android.scrummajster;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class SprintActivity extends AppCompatActivity {


    private static final String SPRINT = "com.ciastkaipiwo.android.scrummajster.sprint";
    private static final String TASK = "com.ciastkaipiwo.android.scrummajster.task";


    private Sprint mSprint;
    private Task mTask;
    private RecyclerView recyclerView;
    private SprintAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mSprint = getIntent().getExtras().getParcelable(SPRINT);
        mTask = getIntent().getExtras().getParcelable(TASK);

        mSprint.mTasksList.add(new Task("Task1",1,5));
        mSprint.mTasksList.add(new Task("Task2",1,7));
        mSprint.mTasksList.add(new Task("Task3",1,4));
        mSprint.mTasksList.add(new Task("Task4",2,4));
        mSprint.mTasksList.add(new Task("Task5",2,3));
        mSprint.mTasksList.add(new Task("Task6",3,1));
        mSprint.mTasksList.add(new Task("Task7",4,1));

        TextView textViewFirs = (TextView) findViewById (R.id.sprint_first_text);
        textViewFirs.setText(getString(R.string.sprint_text));

        TextView textViewStartDate = (TextView) findViewById (R.id.sprint_start_date);
        Date mStartDate = mSprint.getStartDate().getTime();
        DateFormat defaultDateS = DateFormat.getDateInstance();
        String startDateString = defaultDateS.format( mStartDate );
        textViewStartDate.setText(getString(R.string.const_start_date)+" "+startDateString);
        TextView textViewEndDate = (TextView) findViewById (R.id.sprint_end_date);
        Date mEndDate = mSprint.getEndDate().getTime();
        DateFormat defaultDate = DateFormat.getDateInstance();
        String endDateString = defaultDate.format( mEndDate );
        textViewEndDate.setText(getString(R.string.const_end_date)+" "+endDateString);

        TextView textViewTask = (TextView) findViewById (R.id.sprint_task_text);

        if (mSprint.mTasksList.isEmpty())
        {
            textViewTask.setText(getString(R.string.empty_sprint));
        }
        else {textViewTask.setText(getString(R.string.noempty_sprint));}



        mAdapter = new SprintAdapter(mSprint.getTasksList());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }

    public static Intent newIntent(Context packageContext, Sprint sprint){
        Intent intent = new Intent(packageContext,SprintActivity.class);
        intent.putExtra(SPRINT,sprint);
        return intent;
    }

}
