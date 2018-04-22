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
    private static final String TASK_TO_EDIT = "com.ciastkaipiwo.android.scrummajster.task_to_edit";
    private static final String OLD_TASK = "com.ciastkaipiwo.android.scrummajster.old_task";

    private Button mOK;
    private EditText mName;
    private EditText mWeight;
    private EditText mTime;
    private Task mTaskToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_config);

        mOK = (Button) findViewById(R.id.OK);
        mName = (EditText) findViewById(R.id.name);
        mWeight = (EditText) findViewById(R.id.weight);
        mTime = (EditText) findViewById(R.id.time);
        mTaskToEdit = getIntent().getParcelableExtra(TASK_TO_EDIT);
        if (mTaskToEdit != null) {
            mName.setText(mTaskToEdit.getStory());
            mWeight.setText(String.valueOf(mTaskToEdit.getWeight()));
            mTime.setText(String.valueOf(mTaskToEdit.getTime()));
        }

        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = mName.getText().toString();
                String WeightS = mWeight.getText().toString();
                int Weight = Integer.parseInt(WeightS);
                String TimeS = mTime.getText().toString();
                int Time = Integer.parseInt(TimeS);
                Task newTask = new Task(0, Name, Weight, Time);
                Intent data = new Intent();
                data.putExtra(EXTRA_TASK, newTask);
                data.putExtra(OLD_TASK, mTaskToEdit);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    public static Task getNewTask(Intent result) {
        return (Task) result.getParcelableExtra(EXTRA_TASK);
    }

    public static Task getOldTask(Intent result) {
        return (Task) result.getParcelableExtra(OLD_TASK);
    }

    public static Intent newIntent(Context packageContext, Task task){
        Intent intent = new Intent(packageContext, TaskConfigActivity.class);
        intent.putExtra(TASK_TO_EDIT,task);
        return intent;
    }

}


