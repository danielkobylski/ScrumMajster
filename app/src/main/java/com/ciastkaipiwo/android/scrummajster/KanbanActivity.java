package com.ciastkaipiwo.android.scrummajster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class KanbanActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final String SPRINT_TASK = "com.ciastkaipiwo.android.scrummajster.sprinTask";
    private static final String SPRINT_TASK_POSITION = "com.ciastkaipiwo.android.scrummajster.sprinTaskPosition";
    public String mUrl = "http://s12.mydevil.net:8080/";

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

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager mLayoutManagerDoing = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager mLayoutManagerDone = new LinearLayoutManager(getApplicationContext());

        mOkButton = (Button) findViewById(R.id.to_do_ok_button);
        mEnterTask = (EditText) findViewById(R.id.to_do_edit_text);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_to_do);
        mRecyclerViewDoing = (RecyclerView) findViewById(R.id.recycler_view_doing);
        mRecyclerViewDone = (RecyclerView) findViewById(R.id.recycler_view_done);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerViewDoing.setLayoutManager(mLayoutManagerDoing);
        mRecyclerViewDone.setLayoutManager(mLayoutManagerDone);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerViewDoing);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerViewDone);

        initMiniTask();
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMiniTask(mEnterTask.getText().toString());
                mToDoList.add(new MiniTasks(0, mSprintTask.getId() , mEnterTask.getText().toString(),0));
                mEnterTask.getText().clear();
                //mAdapterToDo.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapterToDo);


            }
        });

        mAdapterToDo = new ToDoAdapter(mToDoList, new ToDoListener() {
            @Override
            public void imageButtonOnClik(int position) {
                MiniTasks miniTasks = mToDoList.get(position);

                mToDoList.remove(position);
                mDoingList.add(miniTasks);
                setDoingFlag(miniTasks);
                mRecyclerView.setAdapter(mAdapterToDo);
                mRecyclerViewDoing.setAdapter(mAdapterDoing);
            }
        });


        //final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //mRecyclerView.setLayoutManager(mLayoutManager);
        // mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapterDoing = new DoingAdapter(mDoingList, new DoingListener() {
            @Override
            public void DownButtonOnClik(View v, int position) {
                MiniTasks mini = mDoingList.get(position);
                mDoingList.remove(position);
                mDoneList.add(mini);
                setDoneFlag(mini);
                mRecyclerViewDoing.setAdapter(mAdapterDoing);
                mRecyclerViewDone.setAdapter(mAdapterDone);
            }

            @Override
            public void UpButtonOnClick(View v, int position) {
                MiniTasks mini = mDoingList.get(position);
                mDoingList.remove(position);
                mToDoList.add(mini);
                setToDoFlag(mini);
                mRecyclerView.setAdapter(mAdapterToDo);
                mRecyclerViewDoing.setAdapter(mAdapterDoing);

            }
        });

        mAdapterDone = new DoneAdapter(mDoneList, new DoneListener() {
            @Override
            public void UpButtonOnClickDone(View v, int position) {
                MiniTasks mdone = mDoneList.get(position);
                mDoneList.remove(position);
                mDoingList.add(mdone);
                setDoingFlag(mdone);
                mRecyclerViewDone.setAdapter(mAdapterDone);
                mRecyclerViewDoing.setAdapter(mAdapterDoing);

            }
        });

    }


    public static Intent newIntent(Context packageContext, Task task){
        Intent intent = new Intent(packageContext, KanbanActivity.class);
        intent.putExtra(SPRINT_TASK, task);
        return intent;
    }

    public void setToDoFlag(MiniTasks miniTasks){
        JSONObject params = new JSONObject();
        try {
            params.put("kanbanFlag", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, mUrl+"miniTasks/flag/"+miniTasks.getId(), params,
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
            params.put("kanbanFlag", 2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, mUrl+"miniTasks/flag/"+miniTasks.getId(), params,
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
            params.put("kanbanFlag", 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, mUrl+"miniTasks/flag/"+miniTasks.getId(), params,
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
                Request.Method.POST, mUrl+"miniTasks/add", params,
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
                mUrl+"miniTasks/task?taskId="+mSprintTask.getId(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            Log.d("Response", String.valueOf(response.length()));
                            for(int i=0;i<response.length();i++){
                                MiniTasks m = (new MiniTasks (response.getJSONObject(i)));
                                if (m.getKanbanFlag() == 0) {
                                    mToDoList.add(m);
                                }
                                else if (m.getKanbanFlag() == 1) {
                                    mDoingList.add(m);
                                }
                                else {
                                    mDoneList.add(m);
                                }
                            }

                            mRecyclerView.setAdapter(mAdapterToDo);
                            mRecyclerViewDoing.setAdapter(mAdapterDoing);
                            mRecyclerViewDone.setAdapter(mAdapterDone);

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

    public void deleteMiniTask(final int id) {
        new AsyncHttpClient().delete(mUrl+"miniTasks/"+id, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Delete MiniTask result:", "Success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Delete MiniTask result:", "Failure");
            }
        });
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ToDoAdapter.ToDoViewHolder) {
            deleteMiniTask(mToDoList.get(viewHolder.getAdapterPosition()).getId());
            mAdapterToDo.removeItem(viewHolder.getAdapterPosition());
        }
        else if (viewHolder instanceof DoingAdapter.DoingViewHolder) {
            deleteMiniTask(mDoingList.get(viewHolder.getAdapterPosition()).getId());
            mAdapterDoing.removeItem(viewHolder.getAdapterPosition());
        }
        else if (viewHolder instanceof DoneAdapter.DoneViewHolder) {
            deleteMiniTask(mDoneList.get(viewHolder.getAdapterPosition()).getId());
            mAdapterDone.removeItem(viewHolder.getAdapterPosition());
        }
    }

}
