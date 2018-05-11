package com.ciastkaipiwo.android.scrummajster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 0;
    private static final int REQUEST_CODE_EDIT_PROJECT = 2;
    private static final String TEAM = "com.ciastkaipiwo.android.scrummajster.team";
    private static final String USER="com.ciastkaipiwo.android.scrummajster.user";
    public String mUrl = "http://s12.mydevil.net:8080/";

    private List<Project> mProjectList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ProjectsAdapter mProjectsAdapter;
    private FloatingActionButton mAddButton;
    private Team mTeam;
    User mUser;


    @Override
    public void onResume() {
        super.onResume();

        if (getIntent().getIntExtra("refresher", -1) == 1) {
            finish();
        }

        initProjectsData();
        mProjectsAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mProjectsAdapter);
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mTeam = getIntent().getParcelableExtra(TEAM);
        mUser = getIntent().getParcelableExtra(USER);

        mProjectsAdapter = new ProjectsAdapter(mProjectList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mProjectsAdapter);

        mAddButton = (FloatingActionButton) findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProjectConfigActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD);
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_ADD) {
            if (data == null) {
                return;
            }
            addProject(ProjectConfigActivity.getNewProject(data), mUser);
        }
        else if (requestCode == REQUEST_CODE_EDIT_PROJECT) {
            if (data == null) {
                return;
            }
            editProject(ProjectConfigActivity.getOldProject(data), ProjectConfigActivity.getNewProject(data));
            for(int i = 0; i<mProjectList.size();i++){
                if (mProjectList.get(i).getId() == ProjectConfigActivity.getOldProject(data).getId()){
                    mProjectList.set(i,ProjectConfigActivity.getNewProject(data));
                }
            }
            mProjectsAdapter.notifyDataSetChanged();
        }
    }

    public static Intent newIntent(Context packageContext, User user){
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(USER, user);
        return intent;
    }

    public void editProject(Project oldProject, Project newProject){
        JSONObject params = new JSONObject();
        try {
            params.put("name", newProject.getTitle());
            params.put("startDate", String.valueOf(newProject.getStartDate().getTimeInMillis()));
            params.put("endDate", String.valueOf(newProject.getEndDate().getTimeInMillis()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, mUrl+"projects/"+oldProject.getId(), params,
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


    public void addProject(Project project, User user) {
        JSONObject params = new JSONObject();
        try {
            params.put("name", project.getTitle());
            params.put("startDate", String.valueOf(project.getStartDate().getTimeInMillis()));
            params.put("endDate", String.valueOf(project.getEndDate().getTimeInMillis()));
            params.put("userId", user.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, mUrl+"projects/add", params,
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



    public void initProjectsData() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        mProjectList.clear();
        // Initialize a new JsonObjectRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mUrl+"projects/user?userId="+ mUser.getUserId(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            Log.d("Response", String.valueOf(response.length()));
                            for(int i=0;i<response.length();i++){
                               mProjectList.add(new Project(response.getJSONObject(i)));
                               mProjectsAdapter.notifyDataSetChanged();
                            }
                            mRecyclerView.setAdapter(mProjectsAdapter);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(
                                MainActivity.this,
                                "Error while getting projects data",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    public static Intent newIntent(Context packageContext, Team team){
        Intent intent = new Intent(packageContext, ProjectActivity.class);
        intent.putExtra(TEAM, team);
        return intent;
    }
}

