package com.ciastkaipiwo.android.scrummajster;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

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


        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSprintTask.getToDo().add(mEnterTask.getText().toString());
                mEnterTask.getText().clear();
                mRecyclerView.setAdapter(mAdapterToDo);

            }
        });

        mAdapterToDo = new ToDoAdapter(mSprintTask.getToDo(), new ToDoListener() {
            @Override
            public void imageButtonOnClik(int position) {
                String mTimeTask = mSprintTask.getToDo().get(position);
                mSprintTask.getToDo().remove(position);
                mSprintTask.getDoing().add(mTimeTask);
                mRecyclerView.setAdapter(mAdapterToDo);
                mRecyclerViewDoing.setAdapter(mAdapterDoing);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mAdapterDoing = new DoingAdapter(mSprintTask.getDoing(), new DoingListener() {
            @Override
            public void DownButtonOnClik(View v, int position) {
                String mTimeTaskDown = mSprintTask.getDoing().get(position);
                mSprintTask.getDoing().remove(position);
                mSprintTask.getDone().add(mTimeTaskDown);
                mRecyclerViewDoing.setAdapter(mAdapterDoing);
                mRecyclerViewDone.setAdapter(mAdapterDone);
            }

            @Override
            public void UpButtonOnClick(View v, int position) {
                String mTimeTaskUp = mSprintTask.getDoing().get(position);
                mSprintTask.getDoing().remove(position);
                mSprintTask.getToDo().add(mTimeTaskUp);
                mRecyclerView.setAdapter(mAdapterToDo);
                mRecyclerViewDoing.setAdapter(mAdapterDoing);

            }
        });
        RecyclerView.LayoutManager mLayoutManagerDoing = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewDoing.setLayoutManager(mLayoutManagerDoing);
        mRecyclerViewDoing.setItemAnimator(new DefaultItemAnimator());
        //mRecyclerViewDoing.setAdapter(mAdapterDoing);

        mAdapterDone = new DoneAdapter(mSprintTask.getDone(), new DoneListener() {
            @Override
            public void UpButtonOnClickDone(View v, int position) {
                String mTimeTaskDone = mSprintTask.getDone().get(position);
                mSprintTask.getDone().remove(position);
                mSprintTask.getDoing().add(mTimeTaskDone);
                mRecyclerViewDone.setAdapter(mAdapterDone);
                mRecyclerViewDoing.setAdapter(mAdapterDoing);

            }
        });
        RecyclerView.LayoutManager mLayoutManagerDone = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewDone.setLayoutManager(mLayoutManagerDone);
        mRecyclerViewDone.setItemAnimator(new DefaultItemAnimator());

    }

    public static Intent newIntent(Context packageContext, Task task){
        Intent intent = new Intent(packageContext, KanbanActivity.class);
        intent.putExtra(SPRINT_TASK, task);
        return intent;
    }

}
