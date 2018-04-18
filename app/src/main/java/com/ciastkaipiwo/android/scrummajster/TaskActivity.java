package com.ciastkaipiwo.android.scrummajster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TASKPROJECT = "com.example.angela.backlogactivity.taskproject";
    private static final int REQUEST_CODE_ADD_SMTH = 1;

    private Task mTask;
    private ImageButton mDelete;
    private ImageButton mEdit;
    private ArrayList<Task> taskList;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        position = getIntent().getIntExtra("position", 0);
        taskList = getIntent().getParcelableArrayListExtra("taskList");
        mTask = getIntent().getParcelableExtra(TASKPROJECT);
        mDelete = (ImageButton) findViewById(R.id.deleteButton);
        mDelete.setOnClickListener(this);
        mEdit = (ImageButton) findViewById(R.id.editButton);
        mDelete.setOnClickListener(this);
    }

    public static Intent newIntent(Context packageContext, Task task) {
        Intent intent = new Intent(packageContext, TaskActivity.class);
        intent.putExtra(TASKPROJECT, task);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        else if (requestCode == REQUEST_CODE_ADD_SMTH) {
            if (data == null) {
                return;
            }
            Toast.makeText(TaskActivity.this, "Bedzie fajnie, jesli zadziala", Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(this, BacklogActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteButton: {
                taskList.remove(position);
                onActivityResult(0,RESULT_OK,null);
                break;
            }
            case R.id.editButton: {

                break;
            }


        }
    }
}

