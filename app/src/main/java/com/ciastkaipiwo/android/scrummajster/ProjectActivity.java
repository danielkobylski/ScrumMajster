package com.ciastkaipiwo.android.scrummajster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class ProjectActivity extends AppCompatActivity {


    private static final String PROJECT = "com.ciastkaipiwo.android.scrummajster.project";

    private static final int REQUEST_CODE_ADD_SPRINT = 1;


    

    private Project mProject;
    private LinearLayout mBacklogContainer;
    private LinearLayout mSprintContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        mProject = getIntent().getParcelableExtra(PROJECT);

        mSprintContainer = (LinearLayout) findViewById(R.id.sprint_container);




        mSprintContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SprintMainActivity.newIntent(ProjectActivity.this, mProject);
                startActivity(intent);
            }
        });

        mBacklogContainer = (LinearLayout) findViewById(R.id.backlog_container);

        mBacklogContainer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = BacklogActivity.newIntent(ProjectActivity.this, mProject);
                startActivity(intent);
            }
        });



    }

    public static Intent newIntent(Context packageContext, Project project){
        Intent intent = new Intent(packageContext, ProjectActivity.class);
        intent.putExtra(PROJECT, project);
        return intent;
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_ADD_SPRINT) {
            if (data == null) {
                return;
            }
            Sprint sprint = SprintConfigActivity.getNewSprint(data);
            mProject.addSprint(sprint);

        }
    }



    }




