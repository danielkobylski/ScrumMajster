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
import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BacklogActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_TASK = 1;
    private static final int REQUEST_CODE_EDIT_TASK = 3;
    private static final int REQUEST_CODE_ADD_TO_SPRINT = 4;

    private static final String PROJECT_ID = "com.ciastkaipiwo.android.scrummajster.project_id";
    private static final String TASK_TO_MOVE = "com.ciastkaipiwo.android.scrummajster.task_to_move";
    private static final String SPRINT_CHOSEN = "com.ciastkaipiwo.android.scrummajster.sprint_chosen";


    private FloatingActionButton mPlus;
    private ProjectsDBHelper mDatabaseHelper;

    private List<Task> mTasksList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TasksAdapter mTasksAdapter;
    private int projectId;


    public void onResume() {
        super.onResume();
        if (getIntent().getIntExtra("refresher", -1) == 1) {
            finish();
        }
        initTasksData();

        mRecyclerView.setAdapter(mTasksAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlog);
        mDatabaseHelper = new ProjectsDBHelper(this);

        projectId = getIntent().getIntExtra(PROJECT_ID, -1);
        System.out.println("BacklogActivity PROJECT ID: " + projectId);

        mRecyclerView = (RecyclerView) findViewById(R.id.backlog_recycler_view);
        mTasksAdapter = new TasksAdapter(mTasksList, projectId);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mTasksAdapter);


        mPlus = (FloatingActionButton) findViewById(R.id.add_task);
        mPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BacklogActivity.this, TaskConfigActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_TASK);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_ADD_TASK) {
            if (data == null) {
                return;
            }
            addTask(projectId,TaskConfigActivity.getNewTask(data));
            Toast.makeText(BacklogActivity.this, "Pomyslnie dodano task: "+String.valueOf(TaskConfigActivity.getNewTask(data).getTime()), Toast.LENGTH_LONG).show();
        }
        else if (requestCode == REQUEST_CODE_EDIT_TASK) {
            if (data == null) {
                return;
            }
            Log.d("task",TaskConfigActivity.getOldTask(data).getStory());
            editTask(TaskConfigActivity.getOldTask(data), TaskConfigActivity.getNewTask(data));
            for(int i = 0; i<mTasksList.size();i++){
                if (mTasksList.get(i).getId() == TaskConfigActivity.getOldTask(data).getId()){
                    mTasksList.set(i,TaskConfigActivity.getNewTask(data));
                }
            }
            mTasksAdapter.notifyDataSetChanged();
        }

        else if (requestCode == REQUEST_CODE_ADD_TO_SPRINT) {
            if (data == null) {
                return;
            }
            Sprint sprintChosen = data.getParcelableExtra(SPRINT_CHOSEN);
            Task taskToMove = data.getParcelableExtra(TASK_TO_MOVE);
            moveTask(taskToMove, sprintChosen.getId());
        }

    }

    public static Intent newIntent(Context packageContext, Project project){
        Intent intent = new Intent(packageContext, BacklogActivity.class);
        intent.putExtra(PROJECT_ID, project.getId());
        return intent;
    }

    /**public void initTasksData() {
        mTasksList.clear();
        Cursor data = mDatabaseHelper.getBacklogTasks(projectId);
        while (data.moveToNext()) {
            int id = data.getInt(0);
            String story = data.getString(3);
            int weight = data.getInt(4);
            int time = data.getInt(5);
            mTasksList.add(new Task(id,story, weight, time));
        }
    }**/

    public void moveTask(Task task, int sprintId){
        JSONObject params = new JSONObject();

        try {
            params.put("sprintId", sprintId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, "http://192.168.8.101:8080/tasks/sprintId/"+task.getId(), params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString() + " i am queen");
                        initTasksData();
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

    public void addTask(int projectId, Task task){
        JSONObject params = new JSONObject();
        try {
            params.put("projectId", projectId);
            //params.put("sprintId",null);
            params.put("story", task.getStory());
            params.put("weight", task.getWeight());
            params.put("time", task.getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, "http://192.168.8.101:8080/tasks/add", params,
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


    public void initTasksData(){
        RequestQueue requestQueue = Volley.newRequestQueue(BacklogActivity.this);
        mTasksList.clear();
        // Initialize a new JsonObjectRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://192.168.8.101:8080/tasks/project/sprintIsNull?projectId="+projectId,
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
                                BacklogActivity.this,
                                "Error while getting projects data",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

}