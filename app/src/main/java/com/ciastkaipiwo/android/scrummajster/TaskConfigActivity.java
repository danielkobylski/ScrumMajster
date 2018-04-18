package com.ciastkaipiwo.android.scrummajster;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TaskConfigActivity extends AppCompatActivity{
    private static final String EXTRA_TASK = "com.example.angela.backlogactivity.extra_task";

    private Button mOK;
    private Button mCancel;
    private EditText mName;
    private EditText mWeight;
    private EditText mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_config);

        mOK = (Button) findViewById(R.id.OK);
        //mCancel = (Button) findViewById(R.id.cancel);
        mName = (EditText) findViewById(R.id.name);
        mWeight = (EditText) findViewById(R.id.weight);
        mTime = (EditText) findViewById(R.id.time);

        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = mName.getText().toString();
                String WeightS = mWeight.getText().toString();
                int Weight = Integer.parseInt(WeightS);
                String TimeS = mTime.getText().toString();
                int Time = Integer.parseInt(TimeS);

                Task newTask = new Task(Name, Weight, Time);
                Intent data = new Intent();
                data.putExtra(EXTRA_TASK, newTask);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        //mCancel.setOnClickListener(new View.OnClickListener() {
        //@Override
        //public void onClick(View v) {
        // finish();
        //}

        // });

    }

    public static Task getNewTask(Intent result) {
        return (Task) result.getParcelableExtra(EXTRA_TASK);
    }
}


