package com.ciastkaipiwo.android.scrummajster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class KanbanActivity extends AppCompatActivity {

    private static final String SPRINT_TASK = "com.ciastkaipiwo.android.scrummajster.sprinTask";
    private static final String SPRINT_TASK_POSITION = "com.ciastkaipiwo.android.scrummajster.sprinTaskPosition";

     private Task mSprintTask;
     private int mTaskPosition;
     private EditText mEnterTask;
     private Button mOkButton;
     private ToDoAdapter mAdapterToDo;
     private DoingAdapter mAdapterDoing;
     private DoneAdapter mAdapterDone;
     private RecyclerView mRecyclerView;
     private RecyclerView mRecyclerViewDoing;
     private RecyclerView mRecyclerViewDone;
     //private ImageButton mToDoArrowButton;
    private List<List<MiniTasks>> mMiniList = new ArrayList<List<MiniTasks>>();
    private List<MiniTasks> mToDoList = new ArrayList<MiniTasks>();
    private List<MiniTasks> mDoingList = new ArrayList<MiniTasks>();
    private List<MiniTasks> mDoneList = new ArrayList<MiniTasks>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanban);

        mSprintTask = getIntent().getParcelableExtra(SPRINT_TASK);

        mOkButton = (Button) findViewById(R.id.to_do_ok_button);
        mEnterTask = (EditText) findViewById(R.id.to_do_edit_text);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_to_do);
        mRecyclerViewDoing = (RecyclerView) findViewById(R.id.recycler_view_doing);
        mRecyclerViewDone = (RecyclerView) findViewById(R.id.recycler_view_done);

        mMiniList.add(mToDoList);
        mMiniList.add(mDoingList);
        mMiniList.add(mDoneList);

        initMiniTask();
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMiniTask(mEnterTask.getText().toString());
                mEnterTask.getText().clear();
                mAdapterToDo.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapterToDo);


            }
        });

        mAdapterToDo = new ToDoAdapter(mMiniList.get(0), new ToDoListener() {
            @Override
            public void imageButtonOnClik(int position) {
                MiniTasks miniTasks = mMiniList.get(0).get(position);

                mMiniList.get(0).remove(position);
                mMiniList.get(1).add(miniTasks);
                setDoingFlag(miniTasks);
                mRecyclerView.setAdapter(mAdapterToDo);
                //mRecyclerViewDoing.setAdapter(mAdapterDoing);
            }
        });
        //final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
       // mRecyclerView.setLayoutManager(mLayoutManager);
       // mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        /**
        mAdapterDoing = new DoingAdapter(mMiniList.get(1), new DoingListener() {
            @Override
            public void DownButtonOnClik(View v, int position) {
                MiniTasks mini = mMiniList.get(1).get(position);
                mMiniList.get(1).remove(position);
                mMiniList.get(2).add(mini);

                mRecyclerViewDoing.setAdapter(mAdapterDoing);
                mRecyclerViewDone.setAdapter(mAdapterDone);
            }

            @Override
            public void UpButtonOnClick(View v, int position) {
                MiniTasks min = mMiniList.get(1).get(position);
                mMiniList.get(1).remove(position);
                mMiniList.get(0).add(min);

                mRecyclerView.setAdapter(mAdapterToDo);
                mRecyclerViewDoing.setAdapter(mAdapterDoing);

            }
        });
        RecyclerView.LayoutManager mLayoutManagerDoing = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewDoing.setLayoutManager(mLayoutManagerDoing);
        mRecyclerViewDoing.setItemAnimator(new DefaultItemAnimator());
        //mRecyclerViewDoing.setAdapter(mAdapterDoing);

        mAdapterDone = new DoneAdapter(mMiniList.get(2), new DoneListener() {
            @Override
            public void UpButtonOnClickDone(View v, int position) {
                MiniTasks mdone = mMiniList.get(2).get(position);
                mMiniList.get(2).remove(position);
                mMiniList.get(1).add(mdone);

                mRecyclerViewDone.setAdapter(mAdapterDone);
                mRecyclerViewDoing.setAdapter(mAdapterDoing);

            }
        });
        RecyclerView.LayoutManager mLayoutManagerDone = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewDone.setLayoutManager(mLayoutManagerDone);
        mRecyclerViewDone.setItemAnimator(new DefaultItemAnimator());
**/
    }


    public static Intent newIntent(Context packageContext, Task task){
        Intent intent = new Intent(packageContext, KanbanActivity.class);
        intent.putExtra(SPRINT_TASK, task);
        return intent;
    }

    public void setToDoFlag(MiniTasks miniTasks){
        JSONObject params = new JSONObject();
        try {
            params.put("kanbanFlag", 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, "http://192.168.8.101:8080/miniTasks/flag/"+miniTasks.getId(), params,
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
    public void setDoneFlag(MiniTasks miniTasks){
        JSONObject params = new JSONObject();
        try {
            params.put("kanbanFlag", 3);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, "http://192.168.8.101:8080/miniTasks/flag/"+miniTasks.getId(), params,
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

    public void setDoingFlag(MiniTasks miniTasks){
        JSONObject params = new JSONObject();
        try {
            params.put("kanbanFlag", 2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, "http://192.168.8.101:8080/miniTasks/flag/"+miniTasks.getId(), params,
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

    public void addMiniTask(String mini){
        JSONObject params = new JSONObject();
        try {
            params.put("taskId", mSprintTask.getId());
            params.put("story", mini);
            params.put("kanbanFlag", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, "http://192.168.8.101:8080/miniTasks/add", params,
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

    public void initMiniTask(){
        RequestQueue requestQueue = Volley.newRequestQueue(KanbanActivity.this);
        mMiniList.clear();
        // Initialize a new JsonObjectRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://192.168.8.101:8080/miniTasks/task?taskId="+mSprintTask.getId(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            Log.d("Response", String.valueOf(response.length()));
                            for(int i=0;i<response.length();i++){
                                MiniTasks m = (new MiniTasks (response.getJSONObject(i)));
                                mMiniList.get(m.getKanbanFlag()).add(m);
                            }

                            mRecyclerView.setAdapter(mAdapterToDo);
                            //mRecyclerView.setAdapter(mAdapterDoing);
                            //mRecyclerView.setAdapter(mAdapterDone);

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
                                KanbanActivity.this,
                                "Error while getting projects data",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }


}
