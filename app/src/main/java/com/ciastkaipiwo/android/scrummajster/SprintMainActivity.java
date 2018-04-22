package com.ciastkaipiwo.android.scrummajster;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import java.util.ArrayList;
import java.util.List;

public class SprintMainActivity extends AppCompatActivity {

    private static final String PROJECT_ID = "com.ciastkaipiwo.android.scrummajster.project_id";
    private static final int REQUEST_CODE_ADD_SPRINT = 0;
    private static int mProjectId;

    private ProjectsDBHelper mDatabaseHelper;
    private FloatingActionButton mAddSprintButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint_main);

        mDatabaseHelper = new ProjectsDBHelper(this);

        mProjectId = getIntent().getIntExtra(PROJECT_ID, -1);

        mAddSprintButton = (FloatingActionButton) findViewById(R.id.add_button_sprint);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new ActiveSprintFragment(), "Active");
        adapter.addFragment(new SprintListFragment_old(), "All");

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mAddSprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SprintMainActivity.this, SprintConfigActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_SPRINT);
            }
        });

    }


    public static Intent newIntent(Context packageContext, Project project){
        Intent intent = new Intent(packageContext, SprintMainActivity.class);
        intent.putExtra(PROJECT_ID, project.getId());
        return intent;
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
            System.out.println(mProjectId );

            mDatabaseHelper.addSprint(mProjectId, SprintConfigActivity.getNewSprint(data));
            Toast.makeText(SprintMainActivity.this, "Pomyslnie dodano", Toast.LENGTH_LONG).show();
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

    public static class SprintListFragment extends Fragment  {


        private RecyclerView mRecyclerView;
        private SprintAdapter mSprintAdapter;

        public SprintListFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);}


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_active_sprint, container, false);

            //mRecyclerView = (RecyclerView) view.findViewById(R.id.sprint_recycler_view);
            //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            //updateUI();

            return view;
        }


    }
}
