package com.ciastkaipiwo.android.scrummajster;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class SprintMainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_SPRINT = 1;
    private static final int REQUEST_CODE_EDIT_SPRINT = 3;
    private static final int REQUEST_CODE_EDIT_TASK = 2;
    private static final String PROJECT_ID = "com.ciastkaipiwo.android.scrummajster.project_id";
    private static final String ACTIVE_SPRINT = "com.ciastkaipiwo.android.scrummajster.active_sprint";

    private FloatingActionButton mAddSprintButton;
    private ProjectsDBHelper mDatabaseHelper;
    private int mProjectId;

    @Override
    public void onResume() {
        super.onResume();
        if (getIntent().getIntExtra("refresher", -1) == 1) {
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint_main);

        mDatabaseHelper = new ProjectsDBHelper(this);

        mProjectId = getIntent().getIntExtra(PROJECT_ID, -1);
        System.out.println("SprintActivity PROJECT ID: " + mProjectId);
        mAddSprintButton = (FloatingActionButton) findViewById(R.id.add_button_sprint);


        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle activeSprint = new Bundle();
        Bundle sprintsList = new Bundle();

        activeSprint.putParcelable(ACTIVE_SPRINT, getActiveSprint());
        activeSprint.putInt(PROJECT_ID, mProjectId);

        sprintsList.putInt(PROJECT_ID, mProjectId);

        SprintActiveFragment activeSprintFragment = new SprintActiveFragment();
        SprintListFragment allSprintsFragment = new SprintListFragment();

        activeSprintFragment.setArguments(activeSprint);
        allSprintsFragment.setArguments(sprintsList);

        adapter.addFragment(activeSprintFragment, "Active");
        adapter.addFragment(allSprintsFragment, "All");

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mAddSprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SprintMainActivity.this, SprintConfigActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_SPRINT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_ADD_SPRINT) {
            if (data == null) {
                return;
            }
            mDatabaseHelper.addSprint(mProjectId, SprintConfigActivity.getNewSprint(data));
        } else if (requestCode == REQUEST_CODE_EDIT_TASK) {
            if (data == null) {
                return;
            }
            mDatabaseHelper.editTask(TaskConfigActivity.getOldTask(data), (TaskConfigActivity.getNewTask(data)));
        }
        else if (requestCode == REQUEST_CODE_EDIT_SPRINT) {
            if (data == null) {
                return;
            }
            mDatabaseHelper.editSprint(SprintConfigActivity.getOldSprint(data), SprintConfigActivity.getNewSprint(data));
        }
    }

    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private Sprint getActiveSprint() {
        Cursor data = mDatabaseHelper.getSprints(mProjectId);
        ArrayList<Sprint> sprints = new ArrayList<Sprint>();
        long today = System.currentTimeMillis();
        while (data.moveToNext()) {
            long startDate = data.getLong(2);
            long endDate = data.getLong(3);
            if (today >= startDate && today <= endDate) {
                int id = data.getInt(0);
                GregorianCalendar gStartDate = new GregorianCalendar();
                GregorianCalendar gEndDate = new GregorianCalendar();

                gStartDate.setTimeInMillis(startDate);
                gEndDate.setTimeInMillis(endDate);
                return new Sprint(id, gStartDate,gEndDate);
            }
        }
        return null;
    }

    public static Intent newIntent(Context packageContext, Project project){
        Intent intent = new Intent(packageContext, SprintMainActivity.class);
        intent.putExtra(PROJECT_ID, project.getId());
        return intent;
    }


}
