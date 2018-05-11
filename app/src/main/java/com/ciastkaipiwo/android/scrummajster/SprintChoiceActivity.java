package com.ciastkaipiwo.android.scrummajster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ciastkaipiwo.android.scrummajster.database.ProjectsDBHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SprintChoiceActivity extends AppCompatActivity {

    private static final String PROJECT_ID = "com.ciastkaipiwo.android.scrummajster.project_id";
    private static final String TASK_TO_MOVE = "com.ciastkaipiwo.android.scrummajster.task_to_move";
    public String mUrl = "http://s12.mydevil.net:8080/";

    private int mProjectId;
    private ProjectsDBHelper mDatabaseHelper;
    private List<Sprint> mSprintsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SprintsAdapter mSprintsAdapter;
    private Task mTaskToMove;


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
        mSprintsAdapter.setTaskToMove(getTaskToMove());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mSprintsAdapter);

    }


    public void initSprintsData() {
        RequestQueue requestQueue = Volley.newRequestQueue(SprintChoiceActivity.this);
        mSprintsList.clear();
        // Initialize a new JsonObjectRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mUrl+"sprints/project?projectId="+mProjectId,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            Log.d("Response", String.valueOf(response.length()));
                            for(int i=0;i<response.length();i++){
                                mSprintsList.add(new Sprint(response.getJSONObject(i)));
                            }

                            mRecyclerView.setAdapter(mSprintsAdapter);
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
                                SprintChoiceActivity.this,
                                "Error while getting projects data",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    public static Intent newIntent(Context packageContext, Task task){
        Intent intent = new Intent(packageContext,SprintChoiceActivity.class);
        intent.putExtra(TASK_TO_MOVE,task);
        return intent;
    }

    public Task getTaskToMove() {
        Task task = getIntent().getParcelableExtra(TASK_TO_MOVE);
        return task;
    }

}
