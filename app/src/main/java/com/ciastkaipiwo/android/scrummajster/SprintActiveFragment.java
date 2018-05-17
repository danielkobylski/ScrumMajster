package com.ciastkaipiwo.android.scrummajster;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class SprintActiveFragment extends Fragment {


    private static final String PROJECT_ID = "com.ciastkaipiwo.android.scrummajster.project_id";
    public String mUrl = "http://s12.mydevil.net:8080/";

    private int mProjectId;
    private Sprint mActiveSprint;

    private TextView mSprintTaskActiveText;


    private List<Task> mTasksList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TasksAdapter mTasksAdapter;



    public SprintActiveFragment() {

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


        mSprintTaskActiveText = (TextView) v.findViewById(R.id.sprint_task_active);
        Bundle bundle = getArguments();
        if (bundle != null) {

            mProjectId = bundle.getInt(PROJECT_ID);
            getActiveSprint();
        }



            mRecyclerView = (RecyclerView) v.findViewById(R.id.active_sprint_recycler_view);
            mTasksAdapter = new TasksAdapter(mTasksList, mProjectId);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());



            mSprintTaskActiveText.setText("Tasks:");





        return v;
    }

    public void initTasksData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        mTasksList.clear();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mUrl+"tasks/sprint?sprintId="+mActiveSprint.getId(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            Log.d("Response", String.valueOf(response.length()));
                            for(int i=0;i<response.length();i++){
                                mTasksList.add(new Task(response.getJSONObject(i)));
                            }
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");


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

                        Toast.makeText(
                                getContext().getApplicationContext(),
                                "Error while getting projects data",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void getActiveSprint() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        final List<Sprint> data = new ArrayList<Sprint>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mUrl+"sprints/project?projectId=" + mProjectId,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            long today = System.currentTimeMillis();
                            Log.d("Response", String.valueOf(response.length()));
                            for (int i = 0; i < response.length(); i++) {
                                Sprint sprint = new Sprint(response.getJSONObject(i));
                                long startDate = sprint.getStartDate().getTimeInMillis();
                                long endDate = sprint.getEndDate().getTimeInMillis();
                                if (today >= startDate && today <= endDate) {

                                    mActiveSprint = sprint;
                                    initTasksData();
                                    mRecyclerView.setAdapter(mTasksAdapter);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(
                                getContext().getApplicationContext(),
                                "Error while getting projects data",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

}
