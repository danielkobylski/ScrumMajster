package com.ciastkaipiwo.android.scrummajster;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class SprintActiveFragment extends Fragment {

    private static final String ACTIVE_SPRINT = "com.ciastkaipiwo.android.scrummajster.active_sprint";
    private static final String PROJECT_ID = "com.ciastkaipiwo.android.scrummajster.project_id";

    private int mProjectId;
    private Sprint mActiveSprint;
    private TextView mActiveSprintStartDate;
    private TextView mActiveSprintEndDate;
    private ProjectsDBHelper mDatabaseHelper;

    private List<Task> mTasksList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TasksAdapter mTasksAdapter;



    public SprintActiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mActiveSprint != null) {
            initTasksData();
            mRecyclerView.setAdapter(mTasksAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_active_sprint, container, false);

        mDatabaseHelper = new ProjectsDBHelper(getContext());

        mActiveSprintStartDate = (TextView) v.findViewById(R.id.active_sprint_start_date);
        mActiveSprintEndDate = (TextView) v.findViewById(R.id.active_sprint_end_date);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mActiveSprint = bundle.getParcelable(ACTIVE_SPRINT);
            mProjectId = bundle.getInt(PROJECT_ID);
        }

        if (mActiveSprint != null) {

            mRecyclerView = (RecyclerView) v.findViewById(R.id.active_sprint_recycler_view);
            mTasksAdapter = new TasksAdapter(mTasksList, mProjectId);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

            initTasksData();
            mRecyclerView.setAdapter(mTasksAdapter);

            mActiveSprintStartDate.setText(mActiveSprint.getStartDate().getTime().toString());
            mActiveSprintEndDate.setText(mActiveSprint.getEndDate().getTime().toString());
        }
        else {
            mActiveSprintStartDate.setText("There are no sprints :(");
        }


        return v;
    }

    public void initTasksData() {
        mTasksList.clear();
        Cursor data = mDatabaseHelper.getSprintTasks(mProjectId,mActiveSprint.getId());
        while (data.moveToNext()) {
            int id = data.getInt(0);
            String story = data.getString(3);
            int weight = data.getInt(4);
            int time = data.getInt(5);
            mTasksList.add(new Task(id,story, weight, time));
        }
    }

}
