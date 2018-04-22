package com.ciastkaipiwo.android.scrummajster;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class SprintChoiceActivity extends AppCompatActivity {

    private static final String PROJECT_ID = "com.ciastkaipiwo.android.scrummajster.project_id";

    private int mProjectId;
    private ProjectsDBHelper mDatabaseHelper;
    private List<Sprint> mSprintsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SprintsAdapter mSprintsAdapter;


    @Override
    public void onResume() {
        super.onResume();
        initSprintsData();
        mRecyclerView.setAdapter(mSprintsAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint_choice);

        mDatabaseHelper = new ProjectsDBHelper(this);

        mProjectId = getIntent().getIntExtra(PROJECT_ID, -1);

        mRecyclerView = (RecyclerView) findViewById(R.id.sprints_recycler_view);
        mSprintsAdapter = new SprintsAdapter(mSprintsList, mProjectId);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mSprintsAdapter);

    }


    public void initSprintsData() {
        mSprintsList.clear();
        Cursor data = mDatabaseHelper.getSprints(mProjectId);
        while (data.moveToNext()) {
            int id = data.getInt(0);
            GregorianCalendar startDate = new GregorianCalendar();
            GregorianCalendar endDate = new GregorianCalendar();
            startDate.setTimeInMillis(data.getLong(2));
            endDate.setTimeInMillis(data.getLong(3));
            mSprintsList.add(new Sprint(id, startDate, endDate));
        }
    }
}
