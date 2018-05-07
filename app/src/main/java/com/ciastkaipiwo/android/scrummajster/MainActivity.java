package com.ciastkaipiwo.android.scrummajster;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 0;
    private static final int REQUEST_CODE_EDIT_PROJECT = 2;

    private List<Project> mProjectList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ProjectsAdapter mProjectsAdapter;
    private FloatingActionButton mAddButton;
    private ProjectsDBHelper mDatabaseHelper;

    @Override
    public void onResume() {
        super.onResume();

        if (getIntent().getIntExtra("refresher", -1) == 1) {
            finish();
        }

        initProjectsData();
        mRecyclerView.setAdapter(mProjectsAdapter);
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabaseHelper = new ProjectsDBHelper(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

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
            addProject(ProjectConfigActivity.getNewProject(data));
        }
        else if (requestCode == REQUEST_CODE_EDIT_PROJECT) {
            if (data == null) {
                return;
            }
            mDatabaseHelper.editProject(ProjectConfigActivity.getOldProject(data), ProjectConfigActivity.getNewProject(data));
        }
    }


    public void addProject(Project project) {
        JSONObject params = new JSONObject();
        try {
            params.put("name", project.getTitle());
            params.put("startDate", String.valueOf(project.getStartDate().getTimeInMillis()));
            params.put("endDate", String.valueOf(project.getEndDate().getTimeInMillis()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, "http://192.168.8.100:8080/projects/add", params,
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

    /* INIT PROJECTDS DATA - SQLITE VERSION
    public void initProjectsData() {
        mProjectList.clear();
        Cursor data = mDatabaseHelper.getProjects();
        while (data.moveToNext()) {
            int id = data.getInt(0);
            System.out.println(id);
            String title = data.getString(1);
            System.out.println(title);
            GregorianCalendar startDate = new GregorianCalendar();
            GregorianCalendar endDate = new GregorianCalendar();
            startDate.setTimeInMillis(data.getLong(2));
            endDate.setTimeInMillis(data.getLong(3));
            mProjectList.add(new Project(id, title, startDate, endDate));
        }
    }
    */

    public void initProjectsData() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        mProjectList.clear();
        // Initialize a new JsonObjectRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://192.168.8.100:8080/projects/all",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            Log.d("Response", String.valueOf(response.length()));
                            for(int i=0;i<response.length();i++){
                               mProjectList.add(new Project(response.getJSONObject(i)));
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
}

