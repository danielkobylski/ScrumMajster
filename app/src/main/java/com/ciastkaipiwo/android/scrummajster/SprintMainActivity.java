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
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SprintMainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_SPRINT = 1;
    private static final int REQUEST_CODE_EDIT_SPRINT = 2;
    private static final int REQUEST_CODE_EDIT_TASK = 3;
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
            addSprint( SprintConfigActivity.getNewSprint(data));
        } else if (requestCode == REQUEST_CODE_EDIT_TASK) {
            if (data == null) {
                return;
            }
            editTask(TaskConfigActivity.getOldTask(data), (TaskConfigActivity.getNewTask(data)));

        }
        else if (requestCode == REQUEST_CODE_EDIT_SPRINT) {
            if (data == null) {
                return;
            }
            editSprint(SprintConfigActivity.getOldSprint(data), SprintConfigActivity.getNewSprint(data));


        }
    }
    public void editSprint(Sprint oldSprint, Sprint newSprint){

        JSONObject params = new JSONObject();

        try {
            params.put("startDate", newSprint.getStartDate());
            params.put("endDate", newSprint.getEndDate());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, "http://192.168.8.101:8080/sprints/"+oldSprint.getId(), params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ResponseError: " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        Volley.newRequestQueue(this).add(jsonObjReq);

    }

    public void editTask(Task oldTask, Task newTask){

        JSONObject params = new JSONObject();

        try {
            params.put("story", newTask.getStory());
            params.put("weight", newTask.getWeight());
            params.put("time", newTask.getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, "http://192.168.8.101:8080/tasks/"+oldTask.getId(), params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ResponseError: " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        Volley.newRequestQueue(this).add(jsonObjReq);
    }

    public void addSprint(Sprint sprint){
        JSONObject params = new JSONObject();
        try {
            params.put("projectId",mProjectId);
            params.put("startDate", String.valueOf(sprint.getStartDate().getTimeInMillis()));
            params.put("endDate", String.valueOf(sprint.getEndDate().getTimeInMillis()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, "http://192.168.8.101:8080/sprints/add", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString() + " i am queen");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ResponseError: " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        Volley.newRequestQueue(this).add(jsonObjReq);

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



    public static Intent newIntent(Context packageContext, Project project){
        Intent intent = new Intent(packageContext, SprintMainActivity.class);
        intent.putExtra(PROJECT_ID, project.getId());
        return intent;
    }


}
