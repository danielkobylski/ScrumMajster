package com.ciastkaipiwo.android.scrummajster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
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
//import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SprintActivity extends AppCompatActivity {

    private static final String PROJECT_ID = "com.ciastkaipiwo.android.scrummajster.project_id";
    private static final String SPRINT = "com.ciastkaipiwo.android.scrummajster.sprint";
    public String mUrl = "http://s12.mydevil.net:8080/";
    private static final int REQUEST_CODE_EDIT_TASK = 3;

    private int mProjectId;
    private Sprint mSprint;
    private TextView mSprintStartDate;
    private TextView mSprintEndDate;
    private TextView mSprintFirstText;

    private TextView mSprintTaskText;

    private List<Task> mTasksList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TasksAdapter mTasksAdapter;

    @Override
    public void onResume() {
        super.onResume();
        initTasksData();
        mRecyclerView.setAdapter(mTasksAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprint);


        mProjectId = getIntent().getIntExtra(PROJECT_ID,-1);
        mSprint = getIntent().getParcelableExtra(SPRINT);
        mSprintStartDate = (TextView) findViewById(R.id.sprint_start_date);
        mSprintEndDate = (TextView) findViewById(R.id.sprint_end_date);
        mSprintFirstText = (TextView) findViewById(R.id.sprint_first_text);
        mSprintTaskText = (TextView) findViewById(R.id.sprint_task_text);

        mRecyclerView = (RecyclerView) findViewById(R.id.sprint_recycler_view);
        mTasksAdapter = new TasksAdapter(mTasksList, mProjectId);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //initTasksData();
        mRecyclerView.setAdapter(mTasksAdapter);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        mSprintFirstText.setText("Time:");
        mSprintStartDate.setText("Start Date: "+ dateFormat.format(mSprint.getStartDate().getTimeInMillis()));
        mSprintEndDate.setText("End Date:   "+dateFormat.format(mSprint.getEndDate().getTimeInMillis()));
        mSprintTaskText.setText("Tasks:");

    }


    public void initTasksData() {
        RequestQueue requestQueue = Volley.newRequestQueue(SprintActivity.this);
        mTasksList.clear();
        // Initialize a new JsonObjectRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mUrl+"tasks/sprint?sprintId="+mSprint.getId(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            Log.d("Response", String.valueOf(response.length()));
                            for(int i=0;i<response.length();i++){
                                mTasksList.add(new Task(response.getJSONObject(i)));
                            }
                            mTasksAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(mTasksAdapter);
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
                                SprintActivity.this,
                                "Error while getting projects data",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
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
            editTask(TaskConfigActivity.getOldTask(data), (TaskConfigActivity.getNewTask(data)));
            for(int i = 0; i<mTasksList.size();i++){
                if (mTasksList.get(i).getId() == TaskConfigActivity.getOldTask(data).getId()){
                    mTasksList.set(i,TaskConfigActivity.getNewTask(data));
                }
            }
            mTasksAdapter.notifyDataSetChanged();
        }
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
                Request.Method.PUT, mUrl+"tasks/"+oldTask.getId(), params,
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

}
