package com.ciastkaipiwo.android.scrummajster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TASKPROJECT = "com.example.angela.backlogactivity.taskproject";
    private static final int REQUEST_CODE_ADD_SMTH = 1;
    private static final int REQUEST_CODE_EDIT_TASK = 3;

    private Task mTask;
    private ImageButton mDelete;
    private ImageButton mEdit;
    private ArrayList<Task> taskList;
    private int position;
    private ProjectsDBHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mDatabaseHelper = new ProjectsDBHelper(this);
        mTask = getIntent().getParcelableExtra(TASKPROJECT);
        System.out.println(mTask.getId());
        mDelete = (ImageButton) findViewById(R.id.deleteButton);
        mDelete.setOnClickListener(this);
        mEdit = (ImageButton) findViewById(R.id.editButton);
        mEdit.setOnClickListener(this);
    }

    public static Intent newIntent(Context packageContext, Task task) {
        Intent intent = new Intent(packageContext, TaskActivity.class);
        intent.putExtra(TASKPROJECT, task);
        return intent;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteButton: {
                mDatabaseHelper.removeTask(mTask);
                finish();
                break;
            }
            case R.id.editButton: {
                Intent intent = TaskConfigActivity.newIntent(this,mTask);
                //Intent intent = new Intent(TaskActivity.this, TaskConfigActivity.class);
                startActivityForResult(intent, REQUEST_CODE_EDIT_TASK);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_EDIT_TASK) {
            if (data == null) {
                return;
            }
            mDatabaseHelper.editTask(mTask, TaskConfigActivity.getNewTask(data));
            finish();
        }
    }
}

