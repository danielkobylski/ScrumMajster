package com.ciastkaipiwo.android.scrummajster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class TeamActivity extends AppCompatActivity {

    private static final String USER="com.ciastkaipiwo.android.scrummajster.user";
    User mUser;
    private List<Team> mTeamList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TeamAdapter mTeamAdapter;
    private FloatingActionButton mAddButton;
    private TeamMember tmember;
    private String mId;

    private static final int REQUEST_CODE_ADD_TEAM = 0;
    private static final int REQUEST_CODE_EDIT_TEAM = 1;

    @Override
    public void onResume() {
        super.onResume();


        initTeamData();
        mRecyclerView.setAdapter(mTeamAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        mUser = getIntent().getParcelableExtra(USER);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_team);

        mAddButton = (FloatingActionButton) findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamActivity.this, TeamConfigActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_TEAM);
            }

        });

        mTeamAdapter = new TeamAdapter(mTeamList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mTeamAdapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_ADD_TEAM) {
            if (data == null) {
                return;
            }
            addTeam(TeamConfigActivity.getNewTeam(data));
            addMember(TeamConfigActivity.getNewTeam(data), mUser);



        }
        else if (requestCode == REQUEST_CODE_EDIT_TEAM) {
            if (data == null) {
                return;
            }
            editTeam(TeamConfigActivity.getOldTeam(data), TeamConfigActivity.getNewTeam(data));
            for(int i = 0; i<mTeamList.size();i++){
                if (mTeamList.get(i).getTeamID() == TeamConfigActivity.getOldTeam(data).getTeamID()){
                    mTeamList.set(i,TeamConfigActivity.getNewTeam(data));
                }
            }
            mTeamAdapter.notifyDataSetChanged();
        }
    }

    public static Intent newIntent(Context packageContext, User user){
        Intent intent = new Intent(packageContext, TeamActivity.class);
        intent.putExtra(USER, user);
        return intent;
    }

    public void addMember(Team team, User user){
        JSONObject params = new JSONObject();

        try {
            params.put("temaId",team.getTeamID());
            params.put("userId",user.getUserId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, "http://192.168.8.101:8080/teamMember/add", params,
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

    public void initTeamData() {
        RequestQueue requestQueue = Volley.newRequestQueue(TeamActivity.this);
        mTeamList.clear();
        // Initialize a new JsonObjectRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://192.168.8.101:8080/team/user?userId="+mUser.getUserId(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            Log.d("Response", String.valueOf(response.length()));
                            for(int i=0;i<response.length();i++){
                                mTeamList.add(new Team(response.getJSONObject(i)));
                            }
                            mRecyclerView.setAdapter(mTeamAdapter);
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
                                TeamActivity.this,
                                "Error while getting projects data",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    public void addTeam(Team team) {
        JSONObject params = new JSONObject();
        try {
            params.put("name", team.getName());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, "http://192.168.8.101:8080/team/add", params,
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

    public void editTeam(Team oldTeam, Team newTeam){
        JSONObject params = new JSONObject();
        try {
            params.put("name", newTeam.getName());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, "http://192.168.8.101:8080/team/"+oldTeam.getTeamID(), params,
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
