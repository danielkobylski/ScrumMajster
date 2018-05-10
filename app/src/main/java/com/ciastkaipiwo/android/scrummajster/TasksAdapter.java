package com.ciastkaipiwo.android.scrummajster;

/**
 * Created by Klaudia on 12.04.2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder>{

    private List<Task> tasksList;
    private int mProjectId;


    public TasksAdapter(List<Task> tasksList, int projectId) {
        this.tasksList = tasksList;
        mProjectId = projectId;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasks_list_row, parent, false);
        return new TaskViewHolder(itemView, tasksList);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        holder.name.setText(tasksList.get(position).getStory());
        holder.weight.setText("Weight: "+String.valueOf(tasksList.get(position).getWeight()));
        holder.time.setText("Time: "+String.valueOf(tasksList.get(position).getTime()));
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private static final int REQUEST_CODE_EDIT_TASK = 3;
        private static final int REQUEST_CODE_ADD_TO_SPRINT = 4;
        private static final String PROJECT_ID = "com.ciastkaipiwo.android.scrummajster.project_id";

        public TextView name;
        public TextView weight;
        public TextView time;
        public int position;
        public ImageButton mTaskMenu;

        public Context context;

        public TaskViewHolder(final View view, List<Task>taskList) {
            super(view);
            view.setOnClickListener(this);
            name = (TextView) view.findViewById(R.id.task_row_story);
            weight = (TextView) view.findViewById(R.id.task_row_weight);
            time = (TextView) view.findViewById(R.id.task_row_time);

            context = view.getContext();
            mTaskMenu = (ImageButton) view.findViewById(R.id.task_menu);
            final PopupMenu pum = new PopupMenu(view.getContext(),mTaskMenu);

            String currentActivity = view.getContext().getClass().getName();
            System.out.println(currentActivity);
            if (currentActivity.equals("com.ciastkaipiwo.android.scrummajster.BacklogActivity")) {
                pum.inflate(R.menu.backlog_task_menu);
            } else {
                pum.inflate(R.menu.sprint_task_menu);
            }

            pum.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.task_edit_button: {
                            Intent intent = TaskConfigActivity.newIntent(view.getContext(),tasksList.get(position));
                            ((Activity) view.getContext()).startActivityForResult(intent, REQUEST_CODE_EDIT_TASK);
                            break;
                        }
                        case R.id.task_delete_button: {
                            removeTask(position);
                            //view.getContext().startActivity(tempIntent);
                            //tasksList.remove(position);
                            //notifyDataSetChanged();
                            break;
                        }
                        case R.id.task_add_to_sprint_button: {
                            Intent intent = SprintChoiceActivity.newIntent(view.getContext(),tasksList.get(position));
                            intent.putExtra(PROJECT_ID, mProjectId);
                            ((Activity) view.getContext()).startActivityForResult(intent, REQUEST_CODE_ADD_TO_SPRINT);
                            break;
                        }

                        case R.id.task_add_to_backlog_button: {
                            moveTask(tasksList.get(position));
                            tasksList.remove(position);
                            notifyDataSetChanged();
                            break;
                        }
                    }
                    return true;
                }
            });

            mTaskMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pum.show();
                }
            });

        }



        @Override
        public void onClick(View v){
            if(!String.valueOf(v.getContext().getClass()).equals("class com.ciastkaipiwo.android.scrummajster.BacklogActivity")) {
                System.out.println(tasksList.get(this.position).getStory());
                Intent intent = KanbanActivity.newIntent(v.getContext(), tasksList.get(this.position));
                v.getContext().startActivity(intent);
            }
        }

        public void removeTask(final int position){
            new AsyncHttpClient().delete("http://192.168.8.101:8080/tasks/"+tasksList.get(position).getId(), null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    tasksList.remove(position);
                    notifyDataSetChanged();
                    Log.d("Delete Project result:", "Success");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("Delete Project result:", "Failure");
                }
            });

        }

        public void moveTask(Task task){
            JSONObject params = new JSONObject();

            try {
                params.put("sprintId", null);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.PUT, "http://192.168.8.101:8080/tasks/sprintId/"+task.getId(), params,
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

            Volley.newRequestQueue(context).add(jsonObjReq);

        }
    }
}