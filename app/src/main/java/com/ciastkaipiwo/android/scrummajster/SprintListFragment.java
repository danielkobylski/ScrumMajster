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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


public class SprintListFragment extends Fragment {

    private static final String PROJECT_ID = "com.ciastkaipiwo.android.scrummajster.project_id";

    private int mProjectId;
    private ProjectsDBHelper mDatabaseHelper;
    private List<Sprint> mSprintsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SprintsAdapter mSprintsAdapter;

    public SprintListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initSprintsData();
        mRecyclerView.setAdapter(mSprintsAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_sprint_list, container, false);
        mDatabaseHelper = new ProjectsDBHelper(this.getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            mProjectId = bundle.getInt(PROJECT_ID);
        }

        mRecyclerView = (RecyclerView) v.findViewById(R.id.sprints_recycler_view);
        mSprintsAdapter = new SprintsAdapter(mSprintsList, mProjectId);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mSprintsAdapter);

        return v;
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
