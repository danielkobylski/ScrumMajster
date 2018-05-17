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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class SprintListFragment extends Fragment {

    private static final String PROJECT_ID = "com.ciastkaipiwo.android.scrummajster.project_id";
    public String mUrl = "http://s12.mydevil.net:8080/";

    private int mProjectId;

    private List<Sprint> mSprintsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SprintsAdapter mSprintsAdapter;

    public SprintListFragment() {

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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        mSprintsList.clear();

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
                            mSprintsAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(mSprintsAdapter);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){

                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                "Error while getting projects data",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

}
