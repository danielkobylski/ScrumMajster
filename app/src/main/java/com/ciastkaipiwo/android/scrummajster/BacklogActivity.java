package com.ciastkaipiwo.android.scrummajster;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BacklogActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_TASK = 1;
    private FloatingActionButton mPlus;

    private List<Task> NameList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TasksAdapter mTasksAdapter;


    public BacklogActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlog);

        mRecyclerView = (RecyclerView) findViewById(R.id.backlog_recycler_view);
        mTasksAdapter = new TasksAdapter(NameList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mTasksAdapter);


        mPlus = (FloatingActionButton) findViewById(R.id.plus);
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
            NameList.add(TaskConfigActivity.getNewTask(data));
            Toast.makeText(BacklogActivity.this, "Pomyslnie dodano task: "+String.valueOf(TaskConfigActivity.getNewTask(data).getTime()), Toast.LENGTH_LONG).show();
        }
    }
}